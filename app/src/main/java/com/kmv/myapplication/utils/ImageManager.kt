package com.kmv.myapplication.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface
import com.kmv.myapplication.adapters.ImageAdapter
import com.kmv.myapplication.model.AdData
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

object ImageManager {
    private const val MAX_IMAGE_SIZE = 900
    private const val WIDTH = 0
    private const val HEIGHT = 1

    fun getImageSize(uri : Uri, activity: Activity) : List<Int>{
        val inStream = activity.contentResolver.openInputStream(uri)
        /*val fileTemp = File(activity.cacheDir, "temp.tmp")
        if (inStream != null) {
            fileTemp.copyInStreamToFile(inStream)
        }*/
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        //BitmapFactory.decodeFile(fileTemp.path, options)
        /*return if(imageRotation(fileTemp) == 90){
            listOf(options.outHeight, options.outWidth)
        }else{
            listOf(options.outWidth, options.outHeight)
        }*/
        BitmapFactory.decodeStream(inStream, null, options)
        return listOf(options.outWidth, options.outHeight)
    }

/*    private fun File.copyInStreamToFile(inStream: InputStream){
        this.outputStream().use {
            out -> inStream.copyTo(out)
        }
    }*/

/*    private fun imageRotation(imageFile: File) : Int{
        *//*val imageFile = File(uri)*//*
        val exifIntrfs = ExifInterface(imageFile.absolutePath)
        val orientation = exifIntrfs.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return if(orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_270){
            90
        } else{
            0
        }
    }*/

    fun chooseScaleType(image: ImageView, bitmap: Bitmap){
        if (bitmap.width > bitmap.height){
            image.scaleType = ImageView.ScaleType.CENTER_CROP
        }else{
            image.scaleType = ImageView.ScaleType.CENTER_INSIDE
        }
    }

    suspend fun imageResize(uris: ArrayList<Uri>, activity: Activity): List<Bitmap> = withContext(Dispatchers.IO){
        val tempList = ArrayList<List<Int>>()
        val bitmapList = ArrayList<Bitmap>()
        for (n in uris.indices){
            val sizeImage = getImageSize(uris[n], activity)
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
                bitmapList.add(Picasso.get().load(uris[element]).resize(tempList[element][WIDTH],
                    tempList[element][HEIGHT]).get())
            }
        }

        return@withContext bitmapList
    }

    private suspend fun getBitmapFromUris(uris: List<String?>): List<Bitmap> = withContext(Dispatchers.IO){
        val bitmapList = ArrayList<Bitmap>()

        for (element in uris.indices) {
            kotlin.runCatching {
                bitmapList.add(Picasso.get().load(uris[element]).get())
            }
        }

        return@withContext bitmapList
    }

    fun fillImagesArray(adData: AdData, adapter: ImageAdapter){
        val listUris = listOf(adData.mainImage, adData.image2, adData.image3)
        CoroutineScope(Dispatchers.Main).launch {
            val bitMapList = getBitmapFromUris(listUris)
            adapter.update(bitMapList as ArrayList<Bitmap> /* = java.util.ArrayList<android.graphics.Bitmap> */)
        }
    }

}