package com.kmv.myapplication.utils

import android.content.Context
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

object TreatmentCityList {
    fun getAllCountries(context: Context):ArrayList<String>{
        var tempArray = ArrayList<String>()
        try {
            val inputStream : InputStream = context.assets.open("countriesToCities.json")
            val sizeList:Int = inputStream.available()
            val byteArray = ByteArray(sizeList)
            inputStream.read(byteArray)
            val jsonFile: String = String(byteArray)
            val jsonObject = JSONObject(jsonFile)
            val coutryName = jsonObject.names()
            if (coutryName != null){
            for (unit in 0 until coutryName.length()){
                tempArray.add(coutryName.getString(unit))
            }
            }
        }catch (e:IOException){

        }
        return tempArray
    }
}