package com.kmv.myapplication.act

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import com.kmv.myapplication.R
import com.kmv.myapplication.adapters.ImageAdapter
import com.kmv.myapplication.databinding.ActivityDescriptionBinding
import com.kmv.myapplication.model.AdData
import com.kmv.myapplication.utils.ImageManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DescriptionActivity : AppCompatActivity() {
    lateinit var binding: ActivityDescriptionBinding
    lateinit var adapter: ImageAdapter
    private var adData: AdData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        binding.floatingActBttnPhone.setOnClickListener{callAction()}
        binding.floatingActBttnEmail.setOnClickListener{emailAction()}
    }

    private fun init(){
        adapter = ImageAdapter()
        binding.apply {
            vwPagerDescr.adapter = adapter
        }
        getIntentFromMainAct()
    }

    private fun getIntentFromMainAct(){
        adData = intent.getSerializableExtra(OBJECT_AD) as AdData
        if (adData != null)updateUI(adData!!)
    }

    private fun updateUI(adData: AdData) {
        fillImagesArray(adData)
        fillTextViews(adData)
    }

    private fun fillTextViews(adData: AdData) = with(binding){
        txVwDescrMainTitle.text = adData.title
        txVwDescriptionAct.text = adData.description
        txVwDescrPriceValue.text = adData.price
        txVwDescrPhoneValue.text = adData.phone
        txVwDescrEmailValue.text = adData.email
        txVwDescrCountryValue.text = adData.country
        txVwDescrCityValue.text = adData.city
        txVwDescrZipcodeValue.text = adData.zipcode
        txVwDescrDeliveryValue.text = isWithDelivery(adData.withDelivery.toBoolean())
    }

    private fun isWithDelivery(withDelivery: Boolean): String{
        return if (withDelivery) getString(R.string.tx_yes) else getString(R.string.tx_no)
    }

    private fun fillImagesArray(adData: AdData){
        val listUris = listOf(adData.mainImage, adData.image2, adData.image3)
        CoroutineScope(Dispatchers.Main).launch {
            val bitMapList = ImageManager.getBitmapFromUris(listUris)
            adapter.update(bitMapList as ArrayList<Bitmap> /* = java.util.ArrayList<android.graphics.Bitmap> */)
        }
    }

    companion object{
        const val OBJECT_AD = "AD"
    }

    private fun callAction(){
        val callUri = "phone:${adData?.phone}"
        val intntCall = Intent(Intent.ACTION_DIAL)
        intntCall.data = callUri.toUri()
        startActivity(intntCall)
    }

    private fun emailAction(){
        val intntSendEmail = Intent(Intent.ACTION_SEND)
        intntSendEmail.type = "message/rfc822"
        intntSendEmail.apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf(adData?.email))
            putExtra(Intent.EXTRA_SUBJECT, "About your ad - ${arrayOf(adData?.title)}")
            putExtra(Intent.EXTRA_TEXT, "I am interested your ad - ${arrayOf(adData?.description)}")
        }
        try {
            startActivity(Intent.createChooser(intntSendEmail, "Open with that app"))
        }catch (exc: ActivityNotFoundException){

        }
    }
}