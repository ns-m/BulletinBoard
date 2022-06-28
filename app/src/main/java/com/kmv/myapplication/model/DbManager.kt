package com.kmv.myapplication.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class DbManager {
    val db = Firebase.database.getReference("main")
    val auth = Firebase.auth

    fun publishAd(ad: AdData, doneUploadsDataListener: DoneUploadsDataListener){
        /*if (auth.uid != null)db.child(auth.uid!!).child(ad.key ?: "none").child("element").setValue(ad)*/
        if (auth.uid != null)db.child(ad.key ?: "empty").child(auth.uid!!).child("ad")
            .setValue(ad).addOnCompleteListener {
                /*if (it.isSuccessful)*/
                doneUploadsDataListener.onFinish()
            }
    }

    fun getMyAds(readDataCallback: ReadDataCallback?){
        val query = db.orderByValue().equalTo(auth.uid)
        readDataFromDB(query, readDataCallback)
    }

    fun getAllAds(readDataCallback: ReadDataCallback?){
        val query = db.orderByValue().equalTo(auth.uid)
        readDataFromDB(query, readDataCallback)
    }

    private fun readDataFromDB(query: Query, readDataCallback: ReadDataCallback?){
        query.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                val adArray = ArrayList<AdData>()

                for(item in snapshot.children){
                    val ad = item.children.iterator().next().child("ad").getValue(AdData::class.java)
                    if (ad != null)adArray.add(ad)
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
}