package com.kmv.myapplication.utils

import android.content.Context
import java.io.IOException
import java.io.InputStream

object TreatmentCityList {
    fun getAllCountries(context: Context):ArrayList<String>{
        var tempArray = ArrayList<String>()
        try {
            val inputStream : InputStream = context.assets.open("countriesToCities.json")
            val sizeList:Int = inputStream.available()
            val byteArray = ByteArray(sizeList)
        }catch (e:IOException){

        }
        return tempArray
    }
}