package com.kmv.myapplication.act

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.kmv.myapplication.R
import com.kmv.myapplication.databinding.ActivityEditAdsBinding
import com.kmv.myapplication.dialogs_support.DialogSpinner
import com.kmv.myapplication.utils.ImagePicker
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
    //OnClicks functions
    fun onClickSelectCountry(view:View){
        val listCountry = TreatmentCityList.getAllCountries(this)
        dialog.showSpinnerDialog(this, listCountry, binding.textViewSelectCountry)
        if (binding.textViewSelectCity.text.toString() != getString(R.string.select_city)){
            binding.textViewSelectCity.text = getString(R.string.select_city)
        }
    }
    fun onClickSelectCity(view:View){
        val selectedCountry = binding.textViewSelectCountry.text.toString()
        if (selectedCountry != getString(R.string.select_country)){
            val listCity = TreatmentCityList.getCityCountry(selectedCountry, this)
            dialog.showSpinnerDialog(this, listCity, binding.textViewSelectCity)
        }else {
            Toast.makeText(this, R.string.no_selected_country, Toast.LENGTH_LONG).show()
        }

    }
    fun onClickSelectImages(view:View){
        ImagePicker.getImages(this)
        }
}