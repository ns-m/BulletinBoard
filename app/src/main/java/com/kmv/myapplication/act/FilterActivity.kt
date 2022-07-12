package com.kmv.myapplication.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.kmv.myapplication.R
import com.kmv.myapplication.databinding.ActivityFilterBinding
import com.kmv.myapplication.dialogs_support.DialogSpinner
import com.kmv.myapplication.utils.TreatmentCityList

class FilterActivity : AppCompatActivity() {
    lateinit var binding: ActivityFilterBinding
    private val dialog = DialogSpinner()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBarSettings()
        onClickSelectCountry()
        onClickSelectCity()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    private fun onClickSelectCountry() = with(binding){
        textViewSelectCountry.setOnClickListener {
            val listCountry = TreatmentCityList.getAllCountries(this@FilterActivity)
            dialog.showSpinnerDialog(this@FilterActivity, listCountry, textViewSelectCountry)
            if (textViewSelectCity.text.toString() != getString(R.string.select_city)) {
                textViewSelectCity.text = getString(R.string.select_city)
            }
        }
    }

    private fun onClickSelectCity() = with(binding){
        textViewSelectCity.setOnClickListener {
            val selectedCountry = textViewSelectCountry.text.toString()
            if (selectedCountry != getString(R.string.select_country)) {
                val listCity = TreatmentCityList.getCityCountry(selectedCountry, this@FilterActivity)
                dialog.showSpinnerDialog(this@FilterActivity, listCity, textViewSelectCity)
            } else {
                Toast.makeText(this@FilterActivity, R.string.no_selected_country, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onClickFilterDone() = with(binding){
        bttnFilterDone.setOnClickListener {

        }
    }

    private fun createFilter(): String = with(binding){
        val
    }

    fun actionBarSettings(){
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }
}