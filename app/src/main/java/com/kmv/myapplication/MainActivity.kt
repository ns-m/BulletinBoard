package com.kmv.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.kmv.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    private fun init(){
        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.mainContent.toolbarAds, R.string.app_open, R.string.app_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.id_my_ads -> {
                Toast.makeText(this, "Pushed key ${item.itemId.toString()}", Toast.LENGTH_LONG).show()
            }
            R.id.id_ads_car -> {
                Toast.makeText(this, "Pushed key ${item.itemId}", Toast.LENGTH_LONG).show()
            }
            R.id.id_ads_pc -> {
                Toast.makeText(this, "Pushed key ${item.itemId}", Toast.LENGTH_LONG).show()
            }
            R.id.id_ads_smatphone -> {
                Toast.makeText(this, "Pushed key ${item.itemId}", Toast.LENGTH_LONG).show()
            }
            R.id.id_ads_household_appliance -> {
                Toast.makeText(this, "Pushed key ${item.itemId}", Toast.LENGTH_LONG).show()
            }
            R.id.id_set_ac_sign_up-> {
                Toast.makeText(this, "Pushed key ${item.itemId}", Toast.LENGTH_LONG).show()
            }
            R.id.id_set_ac_sign_in -> {
                Toast.makeText(this, "Pushed key ${item.itemId}", Toast.LENGTH_LONG).show()
            }
            R.id.id_set_ac_sign_out -> {
                Toast.makeText(this, "Pushed key ${item.itemId}", Toast.LENGTH_LONG).show()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}