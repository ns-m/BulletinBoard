package com.kmv.myapplication.data

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DbManager {
    val db = Firebase.database.getReference("main")

    fun publishAd(ad: AdData){

    }
}