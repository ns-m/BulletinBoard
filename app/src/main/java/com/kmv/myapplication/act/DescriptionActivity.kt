package com.kmv.myapplication.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kmv.myapplication.adapters.ImageAdapter
import com.kmv.myapplication.databinding.ActivityDescriptionBinding

class DescriptionActivity : AppCompatActivity() {
    lateinit var binding: ActivityDescriptionBinding
    lateinit var adapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun init(){
        adapter = ImageAdapter()
        binding.apply {
            vwPagerDescr.adapter = adapter
        }
        adapter.update()
    }
}