package com.kmv.myapplication.data

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class DbManager {
    val db = Firebase.database.getReference("main")
    val auth = Firebase.auth

    fun publishAd(ad: AdData){
        if (auth.uid != null)db.child(auth.uid!!).child(ad.key ?: "none").child("element").setValue(ad)
    }

    fun readDataFromDB(){
        db.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                val adArray = ArrayList<AdData>()

                for(item in snapshot.children){
                    val ad = item.children.iterator().next().child("ad").getValue(AdData::class.java)
                    if (ad != null)adArray.add(ad)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}