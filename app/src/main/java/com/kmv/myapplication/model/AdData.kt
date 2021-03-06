package com.kmv.myapplication.model

import java.io.Serializable

data class AdData(
    val country: String? = null,
    val city: String? = null,
    val zipcode: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val withDelivery: String? = null,
    val title: String? = null,
    val category: String? = null,
    val price: String? = null,
    val description: String? = null,
    val key: String? = null,
    val uid: String? = null,
    var time: String = "0",
    val mainImage: String? = null,
    val image2: String? = null,
    val image3: String? = null,
    var viewsCounter: String = "0",
    var favorsCounter: String = "0",
    var isFavor: Boolean = false,
    var emailsCounter: String = "0",
    var callsCounter: String = "0"
):Serializable
