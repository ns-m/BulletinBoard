package com.kmv.myapplication.utils

import android.graphics.BitmapFactory
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object ImageManager {
    const val MAX_IMAGE_SIZE = 900
    const val WIDTH = 0
    const val HEIDHT = 1

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

    suspend fun imageResize(uris: List<String>): String = withContext(Dispatchers.IO){
        val tempList = ArrayList<List<Int>>()
        for (n in uris.indices){
            val sizeImage = getImageSize(uris[n])
            val imageRatio = sizeImage[WIDTH].toFloat() / sizeImage[HEIDHT].toFloat()
            if (imageRatio > 1){
                if (sizeImage[WIDTH] > MAX_IMAGE_SIZE){
                    tempList.add(listOf(MAX_IMAGE_SIZE, (MAX_IMAGE_SIZE / imageRatio).toInt()))
                }else{
                    tempList.add(listOf(sizeImage[WIDTH], sizeImage[HEIDHT]))
                }
            }else{
                if (sizeImage[HEIDHT] > MAX_IMAGE_SIZE){
                    tempList.add(listOf((MAX_IMAGE_SIZE * imageRatio).toInt(), MAX_IMAGE_SIZE))
                }else{
                    tempList.add(listOf(sizeImage[WIDTH], sizeImage[HEIDHT]))
                }
            }
        }
        return@withContext "Done"
    }
}