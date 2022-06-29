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

    fun adViewed(adData: AdData){
        dbManager.adViewed(adData)
    }

    fun delItem(adData: AdData){
        dbManager.delAd(adData, object: DbManager.DoneUploadsDataListener{
            override fun onFinish() {
                val updatedList = liveAdsData.value
                updatedList?.remove(adData)
                liveAdsData.postValue(updatedList)
            }
        })
    }
}