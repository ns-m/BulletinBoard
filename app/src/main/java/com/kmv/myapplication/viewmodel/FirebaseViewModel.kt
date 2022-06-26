package com.kmv.myapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kmv.myapplication.model.AdData
import com.kmv.myapplication.model.DbManager

class FirebaseViewModel: ViewModel() {
    private val dbManager = DbManager()
    val liveAdsData = MutableLiveData<ArrayList<AdData>>()
    fun loadAllAds(){
        dbManager.getAllAds(object: DbManager.ReadDataCallback{

            override fun readData(list: ArrayList<AdData>) {
                liveAdsData.value = list
            }

        })
    }

    fun loadMyAds(){
        dbManager.getMyAds(object: DbManager.ReadDataCallback{

            override fun readData(list: ArrayList<AdData>) {
                liveAdsData.value = list
            }

        })
    }
}