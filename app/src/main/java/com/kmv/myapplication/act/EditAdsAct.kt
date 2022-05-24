package com.kmv.myapplication.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.kmv.myapplication.databinding.ActivityEditAdsBinding
import com.kmv.myapplication.utils.TreatmentCityList

class EditAdsAct : AppCompatActivity() {
    private lateinit var binding: ActivityEditAdsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val listCountry = TreatmentCityList.getAllCountries(this)
        /*val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, TreatmentCityList.getAllCountries(this))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.selectCountry.adapter = adapter*/
    }
}