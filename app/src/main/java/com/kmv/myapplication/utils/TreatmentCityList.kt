package com.kmv.myapplication.utils

import android.content.Context
import com.kmv.myapplication.R
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

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
    fun filterListData(list: ArrayList<String>, searchText: String?) : ArrayList<String>{
        val tempList = ArrayList<String>()
        tempList.clear()
        if (searchText == null){
            tempList.add(R.string.spinner_no_matches.toString())
            return tempList
        }
        for (selection: String in list) {
            if (selection.toLowerCase(Locale.ROOT).startsWith(searchText.toLowerCase(Locale.ROOT)))
                tempList.add(selection)
        }
        if (tempList.size == 0) tempList.add(R.string.spinner_no_matches.toString())
        return tempList
    }
}