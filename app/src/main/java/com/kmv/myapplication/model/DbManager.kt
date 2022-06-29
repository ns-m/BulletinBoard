package com.kmv.myapplication.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class DbManager {
    val db = Firebase.database.getReference(MAIN_NODE)
    val auth = Firebase.auth

    fun publishAd(ad: AdData, doneUploadsDataListener: DoneUploadsDataListener){
        /*if (auth.uid != null)db.child(auth.uid!!).child(ad.key ?: "none").child("element").setValue(ad)*/
        if (auth.uid != null)db.child(ad.key ?: "empty").child(auth.uid!!).child(AD_NODE)
            .setValue(ad).addOnCompleteListener {
                /*if (it.isSuccessful)*/
                doneUploadsDataListener.onFinish()
            }
    }

    fun adViewed(ad: AdData){
        var counter = ad.viewsCounter.toInt()
        counter++
        if (auth.uid != null)db.child(ad.key ?: "empty").child(INFO_NODE)
            .setValue(InfoItem(counter.toString(), ad.favoriteCounter, ad.emailsCounter, ad.callsCounter))
    }

    fun getMyAds(readDataCallback: ReadDataCallback?){
        val query = db.orderByChild(auth.uid + "/ad/uid").equalTo(auth.uid)
        readDataFromDB(query, readDataCallback)
    }

    fun getAllAds(readDataCallback: ReadDataCallback?){
        val query = db.orderByChild(auth.uid + "/ad/price").equalTo(auth.uid)
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
                    ad?.viewsCounter = infoItem?.viewsCounter ?: "0"
                    ad?.favoriteCounter = infoItem?.favoriteCounter ?: "0"
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
        const val INFO_NODE = "info"
        const val MAIN_NODE = "main"
    }
}