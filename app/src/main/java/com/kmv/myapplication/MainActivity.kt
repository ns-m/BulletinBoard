package com.kmv.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kmv.myapplication.act.EditAdsAct
import com.kmv.myapplication.adapters.AdsRecyclerViewAdapter
import com.kmv.myapplication.authentication.AccountAuthentication
import com.kmv.myapplication.databinding.ActivityMainBinding
import com.kmv.myapplication.constants.DialogConsts
import com.kmv.myapplication.dialogs_support.DialogSupport
import com.kmv.myapplication.constants.GoogleAccConsts
import com.kmv.myapplication.model.AdData
import com.kmv.myapplication.viewmodel.FirebaseViewModel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, AdsRecyclerViewAdapter.ItemListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var txVwAccount: TextView
    private val dialogSupport = DialogSupport(this)
    val mainAuth = Firebase.auth
    //val dbManager = DbManager(this)
    val adapter = AdsRecyclerViewAdapter(this)
    private val firebaseViewModel: FirebaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initRecyclerView()
        //dbManager.readDataFromDB()
        initViewModel()
        firebaseViewModel.loadAllAds()
        bottomMenuOnClick()
    }

    override fun onResume() {
        super.onResume()
        binding.mainContent.bttmNavVwMainContent.selectedItemId = R.id.id_home_bottom_main_menu
    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }*/

/*    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GoogleAccConsts.SIGN_IN_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    dialogSupport.accAuth.signInFirebaseWithGoogle(account.idToken!!)
                }
            } catch (e: ApiException) {
                Log.d("MyLog", "Api error : ${e.message}")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
        uiUpdate(mainAuth.currentUser)
    }

    private fun initViewModel(){
        firebaseViewModel.liveAdsData.observe(this, {
            adapter.updateAdapter(it)
            binding.mainContent.layoutNoFavorAds.visibility = if(it.isEmpty()) View.VISIBLE
            else View.GONE
        })
    }

    private fun init() {
        setSupportActionBar(binding.mainContent.toolbarAds)
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.mainContent.toolbarAds,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)
        txVwAccount = binding.navView.getHeaderView(0).findViewById(R.id.tvHeaderUserName)
    }

    private fun bottomMenuOnClick()= with(binding){
        mainContent.bttmNavVwMainContent.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.id_new_ad -> {
                    val intent = Intent(this@MainActivity, EditAdsAct::class.java)
                    startActivity(intent)
                }
                R.id.id_my_ads -> {
                    firebaseViewModel.loadMyAds()
                    mainContent.toolbarAds.title = getString(R.string.my_ads)
                }
                R.id.id_my_favorite_ads -> {
                    firebaseViewModel.loadMyFavors()
                    mainContent.toolbarAds.title = getString(R.string.my_favorite_ads)
                }
                R.id.id_home_bottom_main_menu -> {
                    firebaseViewModel.loadAllAds()
                    mainContent.toolbarAds.title = getString(R.string.main_ads_screen)
                }
            }
            true
        }
    }

    private fun initRecyclerView(){
        binding.apply {
            mainContent.RVmainContent.layoutManager = LinearLayoutManager(this@MainActivity)
            mainContent.RVmainContent.adapter = adapter
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.id_my_ads -> {
                Toast.makeText(this, "Pushed key ${item.itemId.toString()}", Toast.LENGTH_LONG)
                    .show()
            }
            R.id.id_ads_car -> {
                Toast.makeText(this, "Pushed key ${item.itemId}", Toast.LENGTH_LONG).show()
            }
            R.id.id_ads_pc -> {
                Toast.makeText(this, "Pushed key ${item.itemId}", Toast.LENGTH_LONG).show()
            }
            R.id.id_ads_smartphone -> {
                Toast.makeText(this, "Pushed key ${item.itemId}", Toast.LENGTH_LONG).show()
            }
            R.id.id_ads_household_appliance -> {
                Toast.makeText(this, "Pushed key ${item.itemId}", Toast.LENGTH_LONG).show()
            }
            R.id.id_set_ac_sign_up -> {
                dialogSupport.createSingDialog(DialogConsts.SIGN_UP_STATE)
            }
            R.id.id_set_ac_sign_in -> {
                dialogSupport.createSingDialog(DialogConsts.SIGN_IN_STATE)
            }
            R.id.id_set_ac_sign_out -> {
                if (mainAuth.currentUser?.isAnonymous == true) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    return true
                }
                uiUpdate(null)
                mainAuth.signOut()
                dialogSupport.accAuth.singOutGoogle()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun uiUpdate(user: FirebaseUser?) {
        /*txVwAccount.text = if (user == null) {
            resources.getString(R.string.not_regs)
        } else {
            user.email
        }*/
        if (user == null) {
            dialogSupport.accAuth.signInAnonym(object: AccountAuthentication.Listener{
                override fun onComplete() {
                    txVwAccount.setText(R.string.sign_in_anonym)
                    //txVwAccount.text = getString(R.string.sign_in_anonym)
                }
            })
        } else if (user.isAnonymous){
            txVwAccount.setText(R.string.sign_in_anonym)
            //user.email
        } else if (!user.isAnonymous){
            txVwAccount.text = user.email
        }
    }

    /*override fun readData(list: List<AdData>) {
        adapter.updateAdapter(list)
    }*/

    companion object{
        const val EDIT_STATE = "edit_state"
        const val ADS_DATA = "ads_data"
    }

    override fun onDelItem(ad: AdData) {
        firebaseViewModel.delItem(ad)
    }

    override fun onAdViewed(ad: AdData) {
        firebaseViewModel.adViewed(ad)
    }

    override fun onFavorClicked(ad: AdData) {
        firebaseViewModel.onFavorClick(ad)
    }
}