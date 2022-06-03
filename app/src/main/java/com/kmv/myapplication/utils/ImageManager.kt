package com.kmv.myapplication.utils

import android.graphics.BitmapFactory
import androidx.exifinterface.media.ExifInterface
import java.io.File

object ImageManager {
    const val MAX_IMAGE_SIZE = 900

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

    fun imageResize(uris: List<String>){
        val tempList = ArrayList<List<Int>>()
        for (n in uris.indices){
            val sizeImage = getImageSize(uris[n])
            val imageRatio = sizeImage[0].toFloat() / sizeImage[1].toFloat()
            if (imageRatio > 1){

            }
        }
    }
}