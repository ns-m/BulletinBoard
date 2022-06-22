package com.kmv.myapplication.act

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kmv.myapplication.R
import com.kmv.myapplication.adapters.ImageAdapter
import com.kmv.myapplication.model.AdData
import com.kmv.myapplication.model.DbManager
import com.kmv.myapplication.databinding.ActivityEditAdsBinding
import com.kmv.myapplication.dialogs_support.DialogSpinner
import com.kmv.myapplication.fragments.FragmentCloseInterface
import com.kmv.myapplication.fragments.ImageListFragment
import com.kmv.myapplication.utils.ImagePicker
import com.kmv.myapplication.utils.ImagePicker.MAX_IMAGE_COUNT
import com.kmv.myapplication.utils.TreatmentCityList


class EditAdsAct : AppCompatActivity(), FragmentCloseInterface {
    var chooseImageFragment: ImageListFragment? = null
    lateinit var binding: ActivityEditAdsBinding
    private val dialog = DialogSpinner()
    lateinit var imageAdapter: ImageAdapter
    private val dbManager = DbManager()
    var editImagePositions = 0
    private var isEditState = false
    //private var ad: Ad? = null

    /*var launcherMultiSelectImages: ActivityResultLauncher<Intent>? = null
    var launcherForSingleSelectImage: ActivityResultLauncher<Intent>? = null*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, TreatmentCityList.getAllCountries(this))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.selectCountry.adapter = adapter*/
        init()
    }

    private fun init() {
        imageAdapter = ImageAdapter()
        binding.viewPagePics.adapter = imageAdapter
        /*launcherMultiSelectImages = ImagePicker.getMultiSelectImages(this)
        launcherForSingleSelectImage = ImagePicker.getLauncherForSingleSelectImage(this)*/
    }

  /*  override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.getOptions(this, ImagePicker.MAX_IMAGE_COUNT, ImagePicker.REQUEST_CODE_GET_IMAGE)
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
    }*/

    class PermUtil {
        object REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS {

        }

    }

    //OnClicks functions
    fun onClickSelectCountry(view: View) {
        val listCountry = TreatmentCityList.getAllCountries(this)
        dialog.showSpinnerDialog(this, listCountry, binding.textViewSelectCountry)
        if (binding.textViewSelectCity.text.toString() != getString(R.string.select_city)) {
            binding.textViewSelectCity.text = getString(R.string.select_city)
        }
    }

    fun onClickSelectCity(view: View) {
        val selectedCountry = binding.textViewSelectCountry.text.toString()
        if (selectedCountry != getString(R.string.select_country)) {
            val listCity = TreatmentCityList.getCityCountry(selectedCountry, this)
            dialog.showSpinnerDialog(this, listCity, binding.textViewSelectCity)
        } else {
            Toast.makeText(this, R.string.no_selected_country, Toast.LENGTH_LONG).show()
        }

    }

    fun onClickSelectImages(view: View) {
//        ImagePicker.getImages(this)
        if (imageAdapter.mainArray.size == 0){
            ImagePicker.getMultiImages(this, MAX_IMAGE_COUNT)
        }else{
            openChooseImageFragment(null)
            chooseImageFragment?.updateAdapterFromEdit(imageAdapter.mainArray)
        }

    }

    fun onClickSelectCategory(view: View){
        val selectCategory= binding.textViewSelectCategory.text.toString()
        val listCategory = resources.getStringArray(R.array.category).toMutableList() as ArrayList
        dialog.showSpinnerDialog(this, listCategory, binding.textViewSelectCategory)
    }

    fun onClickPublish(view: View){
        dbManager.publishAd(fillAd())
    }

    private fun fillAd(): AdData{
        val ad: AdData
        binding.apply {
            ad = AdData(textViewSelectCountry.text.toString(), textViewSelectCity.text.toString(),
                textViewInputZipcode.text.toString(), textViewInputPhone.text.toString(),
                checkDelivery.isChecked.toString(), textViewAdTitle.text.toString(),
                textViewSelectCategory.text.toString(), textViewInputPrice.text.toString(),
                textViewDescription.text.toString(), dbManager.db.push().key, dbManager.auth.uid)
        }
        return ad
    }

////
    override fun onFragmentClose(list: ArrayList<Bitmap>) {
        binding.scrollViewMain.visibility = View.VISIBLE
        imageAdapter.update(list)
        chooseImageFragment = null
    }

    fun openChooseImageFragment(newList: ArrayList<Uri>?){
        chooseImageFragment = ImageListFragment(this, newList)
        binding.scrollViewMain.visibility = View.GONE
        val fragmentManager = supportFragmentManager.beginTransaction()
        fragmentManager.replace(R.id.placeHolder, chooseImageFragment!!)
        fragmentManager.commit()
    }
}