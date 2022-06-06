package com.kmv.myapplication.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.exifinterface.media.ExifInterface
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object ImageManager {
    private const val MAX_IMAGE_SIZE = 900
    private const val WIDTH = 0
    private const val HEIGHT = 1

    fun getImageSize(uri : String) : List<Int>{
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(uri, options)
        return if(imageRotation(uri) == 90){
            listOf(options.outHeight, options.outWidth)
        }else{
            listOf(options.outWidth, options.outHeight)
        }
    }

    private fun imageRotation(uri: String) : Int{
        val imageFile = File(uri)
        val exifIntrfs = ExifInterface(imageFile.absolutePath)
        val orientation = exifIntrfs.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return if(orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_270){
            90
        } else{
            0
        }
    }

    fun chooseScaleType(image: ImageView, bitmap: Bitmap){
        if (bitmap.width > bitmap.height){
            image.scaleType = ImageView.ScaleType.CENTER_CROP
        }else{
            image.scaleType = ImageView.ScaleType.CENTER_INSIDE
        }
    }

    suspend fun imageResize(uris: List<String>): List<Bitmap> = withContext(Dispatchers.IO){
        val tempList = ArrayList<List<Int>>()
        val bitmapList = ArrayList<Bitmap>()
        for (n in uris.indices){
            val sizeImage = getImageSize(uris[n])
            val imageRatio = sizeImage[WIDTH].toFloat() / sizeImage[HEIGHT].toFloat()
            if (imageRatio > 1){
                if (sizeImage[WIDTH] > MAX_IMAGE_SIZE){
                    tempList.add(listOf(MAX_IMAGE_SIZE, (MAX_IMAGE_SIZE / imageRatio).toInt()))
                }else{
                    tempList.add(listOf(sizeImage[WIDTH], sizeImage[HEIGHT]))
                }
            }else{
                if (sizeImage[HEIGHT] > MAX_IMAGE_SIZE){
                    tempList.add(listOf((MAX_IMAGE_SIZE * imageRatio).toInt(), MAX_IMAGE_SIZE))
                }else{
                    tempList.add(listOf(sizeImage[WIDTH], sizeImage[HEIGHT]))
                }
            }
        }

        for (element in uris.indices) {
            kotlin.runCatching {
                bitmapList.add(Picasso.get().load(File(uris[element])).resize(tempList[element][WIDTH],
                    tempList[element][HEIGHT]).get())
            }
        }

        return@withContext bitmapList
    }
}