package com.kmv.myapplication.model

import java.io.Serializable

data class AdData(
    val country: String? = null,
    val city: String? = null,
    val zipcode: String? = null,
    val phone: String? = null,
    val withDelivery: String? = null,
    val title: String? = null,
    val category: String? = null,
    val price: String? = null,
    val description: String? = null,
    val key: String? = null,
    val uid: String? = null,
    val viewsCounter: String = "0",
    val favoriteCounter: String = "0",
    val emailsCounter: String = "0",
    val callsCounter: String = "0"
):Serializable
