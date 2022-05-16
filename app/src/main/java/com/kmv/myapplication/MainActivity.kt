package com.kmv.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kmv.myapplication.databinding.ActivityMainBinding
import com.kmv.myapplication.dialogs_support.DialogConsts
import com.kmv.myapplication.dialogs_support.DialogSupport
import com.kmv.myapplication.dialogs_support.GoogleAccConsts

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var txVwAccount: TextView
    private val dialogSupport = DialogSupport(this)
    val mainAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == GoogleAccConsts.SIGN_IN_REQUEST_CODE)
        super.onActivityResult(requestCode, resultCode, data)
    }
    override fun onStart(){
        super.onStart()
        uiUpdate(mainAuth.currentUser)
    }
    private fun init(){
        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.mainContent.toolbarAds, R.string.app_open, R.string.app_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)
        txVwAccount = binding.navView.getHeaderView(0).findViewById(R.id.tvHeaderUserName)
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
                dialogSupport.createSingDialog(DialogConsts.SIGN_UP_STATE)
            }
            R.id.id_set_ac_sign_in -> {
                dialogSupport.createSingDialog(DialogConsts.SIGN_IN_STATE)
            }
            R.id.id_set_ac_sign_out -> {
                uiUpdate(null)
                mainAuth.signOut()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    fun uiUpdate(user: FirebaseUser?){
        txVwAccount.text = if(user == null){
            resources.getString(R.string.app_not_regs)
        }else{
            user.email
        }
    }
}