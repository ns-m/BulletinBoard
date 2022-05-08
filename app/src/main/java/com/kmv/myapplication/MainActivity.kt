package com.kmv.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_content.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }
    private fun init(){
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbarAds, R.string.app_open, R.string.app_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }
}