package com.kmv.myapplication.act

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init(){
        adapter = ImageAdapter()
        binding.apply {
            vwPagerDescr.adapter = adapter
        }
        getIntentFromMainAct()
    }

    private fun getIntentFromMainAct(){
        val ad = intent.getSerializableExtra(OBJECT_AD) as AdData
        fillImagesArray(ad)
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
}