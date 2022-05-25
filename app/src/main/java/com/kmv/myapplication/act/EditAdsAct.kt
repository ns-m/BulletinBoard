package com.kmv.myapplication.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.kmv.myapplication.databinding.ActivityEditAdsBinding
import com.kmv.myapplication.dialogs_support.DialogSpinner
import com.kmv.myapplication.utils.TreatmentCityList

class EditAdsAct : AppCompatActivity() {
    lateinit var binding: ActivityEditAdsBinding
    private val dialog = DialogSpinner()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, TreatmentCityList.getAllCountries(this))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.selectCountry.adapter = adapter*/
        init()
    }
    private fun init(){

    }
    //OnClicks functions
    fun onClickSelectCountry(view:View){
        val listCountry = TreatmentCityList.getAllCountries(this)
        dialog.showSpinnerDialog(this, listCountry)
    }
}