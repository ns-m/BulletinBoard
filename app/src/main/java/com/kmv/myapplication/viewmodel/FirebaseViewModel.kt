package com.kmv.myapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kmv.myapplication.model.AdData
import com.kmv.myapplication.model.DbManager

class FirebaseViewModel: ViewModel() {
    private val dbManager = DbManager()
    val liveAdsData = MutableLiveData<ArrayList<AdData>>()

    fun loadAllAdsFirstPage() {
        dbManager.getAllAdsFirstPage(object : DbManager.ReadDataCallback {
            override fun readData(list: ArrayList<AdData>) {
                liveAdsData.value = list
            }
        })
    }

    fun loadAllAdsNextPage(time: String) {
        dbManager.getAllAdsNextPage(time, object : DbManager.ReadDataCallback {
            override fun readData(list: ArrayList<AdData>) {
                liveAdsData.value = list
            }
        })
    }

    fun loadAllAdsFromCat(cat: String) {
        dbManager.getAllAdsFromCatFirstPage(cat, object : DbManager.ReadDataCallback {
            override fun readData(list: ArrayList<AdData>) {
                liveAdsData.value = list
            }
        })
    }

    fun loadAllAdsFromCatNextPage(catTime: String) {
        dbManager.getAllAdsFromCatNextPage(catTime, object : DbManager.ReadDataCallback {
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

    fun loadMyFavors(){
        dbManager.getMyFavors(object: DbManager.ReadDataCallback{

            override fun readData(list: ArrayList<AdData>) {
                liveAdsData.value = list
            }
        })
    }

    fun onFavorClick(adData: AdData){
        dbManager.onFavorClick(adData, object: DbManager.DoneUploadsDataListener{
            override fun onFinish() {
                val updatedList = liveAdsData.value
                val postn = updatedList?.indexOf(adData)
                if (postn != -1){
                    postn?.let {
                        val favorCounter = if (adData.isFavor) adData.favorsCounter.toInt() - 1 else
                            adData.favorsCounter.toInt() + 1
                        updatedList[postn] = updatedList[postn].copy(isFavor = !adData.isFavor,
                        favorsCounter = favorCounter.toString())
                    }
                }
                liveAdsData.postValue(updatedList)
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