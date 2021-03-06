package com.kmv.myapplication.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class DbManager {
    val db = Firebase.database.getReference(MAIN_NODE)
    val dbStorage = Firebase.storage.getReference(MAIN_NODE)
    val auth = Firebase.auth

    fun publishAd(ad: AdData, doneUploadsDataListener: DoneUploadsDataListener){
        /*if (auth.uid != null)db.child(auth.uid!!).child(ad.key ?: "none").child("element").setValue(ad)*/
        if (auth.uid != null)db.child(ad.key ?: "empty").child(auth.uid!!).child(AD_NODE)
            .setValue(ad).addOnCompleteListener {
                val adFilter = AdFilter(ad.time, "${ad.category}_${ad.time}")
                db.child(ad.key ?: "empty").child(auth.uid!!).child(FILTER_NODE)
                    .setValue(adFilter).addOnCompleteListener{
                /*if (it.isSuccessful)*/
                doneUploadsDataListener.onFinish()
                    }
            }
    }

    fun adViewed(ad: AdData){
        var counter = ad.viewsCounter.toInt()
        counter++
        if (auth.uid != null)db.child(ad.key ?: "empty").child(INFO_NODE)
            .setValue(InfoItem(counter.toString(), ad.emailsCounter, ad.callsCounter))
    }

    fun onFavorClick(ad: AdData, listener: DoneUploadsDataListener){
        if (ad.isFavor){
            delFromFavors(ad, listener)
        }else{
            addToFavors(ad, listener)
        }
    }

    private fun addToFavors(ad: AdData, listener: DoneUploadsDataListener){
        ad.key?.let {
            auth.uid?.let {
                    uid -> db.child(it).child(FAVORS_NODE).child(uid).setValue(uid)
                .addOnCompleteListener {
                if (it.isSuccessful) listener.onFinish()
            }
            }
        }
    }

    private fun delFromFavors(ad: AdData, listener: DoneUploadsDataListener){
        ad.key?.let {
            auth.uid?.let {
                    uid -> db.child(it).child(FAVORS_NODE).child(uid).removeValue()
                .addOnCompleteListener {
                    if (it.isSuccessful) listener.onFinish()
                }
            }
        }
    }

    fun getMyAds(readDataCallback: ReadDataCallback?){
        val query = db.orderByChild(auth.uid + "/ad/uid").equalTo(auth.uid)
        readDataFromDB(query, readDataCallback)
    }

    fun getMyFavors(readDataCallback: ReadDataCallback?){
        val query = db.orderByChild("/favors/${auth.uid}").equalTo(auth.uid)
        readDataFromDB(query, readDataCallback)
    }

    fun getAllAdsFirstPage(readDataCallback: ReadDataCallback?){
        /*val query = db.orderByChild(auth.uid + "/ad/time").equalTo(auth.uid)*/
        val query = db.orderByChild(FILTER_PATH).limitToLast(ADS_LIMIT)
        readDataFromDB(query, readDataCallback)
    }

    fun getAllAdsNextPage(time: String, readDataCallback: ReadDataCallback?){
        val query = db.orderByChild(FILTER_PATH).endBefore(time).limitToLast(ADS_LIMIT)
        readDataFromDB(query, readDataCallback)
    }

    fun getAllAdsFromCatFirstPage(cat: String, readDataCallback: ReadDataCallback?){
        val query = db.orderByChild(FILTER_CAT_PATH).startAt(cat).endAt(cat + "_\uf8ff")
            .limitToLast(ADS_LIMIT)
        readDataFromDB(query, readDataCallback)
    }

    fun getAllAdsFromCatNextPage(catTime: String, readDataCallback: ReadDataCallback?){
        val query = db.orderByChild(FILTER_CAT_PATH).endBefore(catTime)
            .limitToLast(ADS_LIMIT)
        readDataFromDB(query, readDataCallback)
    }

    fun delAd(ad: AdData, doneUploadsDataListener: DoneUploadsDataListener){
        if (ad.key  != null && ad.uid != null)
        db.child(ad.key).child(ad.uid).removeValue().addOnCompleteListener {
            if (it.isSuccessful) doneUploadsDataListener.onFinish()
        }
    }

    private fun readDataFromDB(query: Query, readDataCallback: ReadDataCallback?){
        query.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                val adArray = ArrayList<AdData>()

                for(item in snapshot.children){
                    var ad: AdData? = null
                    item.children.forEach {
                        if (ad == null) ad = it.child(AD_NODE).getValue(AdData::class.java)
                    }
                    val infoItem = item.child(INFO_NODE).getValue(InfoItem::class.java)
                    val favorsCounter = item.child(FAVORS_NODE).childrenCount
                    val isFavor = auth.uid?.let { item.child(FAVORS_NODE).child(it).getValue(String::class.java) }

                    ad?.isFavor = isFavor != null
                    ad?.viewsCounter = infoItem?.viewsCounter ?: "0"
                    ad?.favorsCounter = favorsCounter.toString()
                    ad?.emailsCounter = infoItem?.emailsCounter ?: "0"
                    ad?.callsCounter = infoItem?.callsCounter ?: "0"
                    if (ad != null)adArray.add(ad!!)
                }
                readDataCallback?.readData(adArray)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    interface ReadDataCallback {
        fun readData(list: ArrayList<AdData>)
    }

    interface DoneUploadsDataListener{
        fun onFinish()
    }

    companion object{
        const val AD_NODE = "ad"
        const val FILTER_NODE = "adFilter"
        const val INFO_NODE = "info"
        const val MAIN_NODE = "main"
        const val FAVORS_NODE = "favors"
        const val ADS_LIMIT = 4
        const val FILTER_PATH = "/adFilter/time"
        const val FILTER_CAT_PATH = "/adFilter/catgTime"
    }
}