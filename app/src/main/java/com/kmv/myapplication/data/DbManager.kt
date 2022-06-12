package com.kmv.myapplication.data

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DbManager {
    val db = Firebase.database.getReference("main")
    val auth = Firebase.auth

    fun publishAd(ad: AdData){
        if (auth.uid != null)db.child(auth.uid!!).child(ad.key ?: "none").child("element").setValue(ad)
    }

    fun readDataFromDB(){

    }
}