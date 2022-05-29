package com.kmv.myapplication.act

import android.R.attr
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kmv.myapplication.R
import com.kmv.myapplication.databinding.ActivityEditAdsBinding
import com.kmv.myapplication.dialogs_support.DialogSpinner
import com.kmv.myapplication.fragments.FragmentCloseInterface
import com.kmv.myapplication.fragments.ImageListFragment
import com.kmv.myapplication.utils.ImagePicker
import com.kmv.myapplication.utils.TreatmentCityList


class EditAdsAct : AppCompatActivity(), FragmentCloseInterface{
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
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.getImages(this, 5)
                } else {
                    Toast.makeText(
                        this,
                        "Approve permissions to open Pix ImagePicker",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }

    class PermUtil {
        object REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS {

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == ImagePicker.RequestCode) {
            if (data != null) {
                val returnValue =
                    data.getStringArrayListExtra(Pix.IMAGE_RESULTS) /* = java.util.ArrayList<kotlin.String> */
            }
        }
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
//        ImagePicker.getImages(this)
        binding.scrollViewMain.visibility = View.GONE
        val fragmentManager = supportFragmentManager.beginTransaction()
        fragmentManager.replace(R.id.placeHolder, ImageListFragment(this))
        fragmentManager.commit()
        }

    override fun onFragmentClose() {
        binding.scrollViewMain.visibility = View.VISIBLE
    }
}