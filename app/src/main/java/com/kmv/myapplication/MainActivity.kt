@file:Suppress("OverrideDeprecatedMigration")

package com.kmv.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kmv.myapplication.act.DescriptionActivity
import com.kmv.myapplication.act.EditAdsAct
import com.kmv.myapplication.adapters.AdsRecyclerViewAdapter
import com.kmv.myapplication.authentication.AccountAuthentication
import com.kmv.myapplication.databinding.ActivityMainBinding
import com.kmv.myapplication.constants.DialogConsts
import com.kmv.myapplication.dialogs_support.DialogSupport
import com.kmv.myapplication.model.AdData
import com.kmv.myapplication.viewmodel.FirebaseViewModel
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, AdsRecyclerViewAdapter.ItemListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var txVwAccount: TextView
    private lateinit var imgVwAccount: ImageView
    private val dialogSupport = DialogSupport(this)
    val mainAuth = Firebase.auth
    //val dbManager = DbManager(this)
    val adapter = AdsRecyclerViewAdapter(this)
    lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    private val firebaseViewModel: FirebaseViewModel by viewModels()
    private var clearUpdate: Boolean = true
    private var currentCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initRecyclerView()
        //dbManager.readDataFromDB()
        initViewModel()
        //firebaseViewModel.loadAllAds("0")
        bottomMenuOnClick()
        scrollListener()
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

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
    }*/

    private fun onActivityResult() {
        googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    dialogSupport.accAuth.signInFirebaseWithGoogle(account.idToken!!)
                }
            } catch (e: ApiException) {
                Log.d("MyLog", "Api error : ${e.message}")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        uiUpdate(mainAuth.currentUser)
    }

    private fun initViewModel(){
        firebaseViewModel.liveAdsData.observe(this) {
            val list = getAdsByCategory(it)
            if (!clearUpdate) {
                adapter.updateAdapter(list)
            } else {
                adapter.updateAdapterWithClear(list)
            }
            binding.mainContent.layoutNoFavorAds.visibility =
                if (adapter.itemCount == 0) View.VISIBLE else View.GONE
        }
    }

    private fun getAdsByCategory(list: ArrayList<AdData>): ArrayList<AdData>{
        val tempList = ArrayList<AdData>()
        tempList.addAll(list)
        if (currentCategory != getString(R.string.main_ads_screen)){
            tempList.clear()
            list.forEach {
                if (currentCategory == it.category) tempList.add(it)
            }
        }
        tempList.reverse()
        return tempList
    }

    private fun init() {
        currentCategory = getString(R.string.main_ads_screen)
        setSupportActionBar(binding.mainContent.toolbarAds)
        onActivityResult()
        navVwSettings()
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
        imgVwAccount = binding.navView.getHeaderView(0).findViewById(R.id.ivHeaderUserAva)
    }

    private fun bottomMenuOnClick()= with(binding){
        mainContent.bttmNavVwMainContent.setOnNavigationItemSelectedListener { item ->
            clearUpdate = true
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
                    currentCategory = getString(R.string.main_ads_screen)
                    firebaseViewModel.loadAllAdsFirstPage()
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
        clearUpdate = true
        when (item.itemId) {
            R.id.id_my_ads -> {
                Toast.makeText(this, "Pushed key ${item.itemId.toString()}", Toast.LENGTH_LONG)
                    .show()
            }
            R.id.id_ads_car -> {
                /*Toast.makeText(this, "Pushed key ${item.itemId}", Toast.LENGTH_LONG).show()*/
                getAdsFromCat(getString(R.string.ads_car))

            }
            R.id.id_ads_pc -> {
                /*Toast.makeText(this, "Pushed key ${item.itemId}", Toast.LENGTH_LONG).show()*/
                getAdsFromCat(getString(R.string.ads_pc))
            }
            R.id.id_ads_smartphone -> {
                /*Toast.makeText(this, "Pushed key ${item.itemId}", Toast.LENGTH_LONG).show()*/
                getAdsFromCat(getString(R.string.ads_smartphone))
            }
            R.id.id_ads_household_appliance -> {
                /*Toast.makeText(this, "Pushed key ${item.itemId}", Toast.LENGTH_LONG).show()*/
                getAdsFromCat(getString(R.string.ads_household_appliance))
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

    private fun getAdsFromCat(cat: String){
        currentCategory = cat
        //val catTime = "${cat}_0"
        //firebaseViewModel.loadAllAdsFromCat(catTime)
        firebaseViewModel.loadAllAdsFromCat(cat)
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
                    imgVwAccount.setImageResource(R.drawable.ic_account)
                    //txVwAccount.text = getString(R.string.sign_in_anonym)
                }
            })
        } else if (user.isAnonymous){
            txVwAccount.setText(R.string.sign_in_anonym)
            imgVwAccount.setImageResource(R.drawable.ic_account)
            //user.email
        } else if (!user.isAnonymous){
            txVwAccount.text = user.email
            Picasso.get().load(user.photoUrl).into(imgVwAccount)
        }
    }

    /*override fun readData(list: List<AdData>) {
        adapter.updateAdapter(list)
    }*/

    override fun onDelItem(ad: AdData) {
        firebaseViewModel.delItem(ad)
    }

    override fun onAdViewed(ad: AdData) {
        firebaseViewModel.adViewed(ad)
        val intnt = Intent(this, DescriptionActivity::class.java)
        intnt.putExtra(DescriptionActivity.OBJECT_AD, ad)
        startActivity(intnt)
    }

    override fun onFavorClicked(ad: AdData) {
        firebaseViewModel.onFavorClick(ad)
    }

    private fun navVwSettings() = with(binding){
        val menuSet = navView.menu

        val adsCat = menuSet.findItem(R.id.category_ads)
        val spanAdsCat = SpannableString(adsCat.title)
        spanAdsCat.setSpan(ForegroundColorSpan(ContextCompat.getColor(this@MainActivity,
        R.color.my_color_red)), 0, adsCat.title.length, 0)
        adsCat.title = spanAdsCat

        val accSet = menuSet.findItem(R.id.settings_account)
        val spanAccSet = SpannableString(accSet.title)
        spanAccSet.setSpan(ForegroundColorSpan(ContextCompat.getColor(this@MainActivity,
            R.color.my_color_red)), 0, accSet.title.length, 0)
        accSet.title = spanAccSet
    }

    private fun scrollListener() = with(binding.mainContent){
        RVmainContent.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recView, newState)
                if (!recView.canScrollVertically(SCROLL_DOWN) && newState ==
                    RecyclerView.SCROLL_STATE_IDLE){
                    clearUpdate = false
                    val adsList = firebaseViewModel.liveAdsData.value!!
                    if (adsList.isNotEmpty()) {
                        /*adsList[adsList.size - 1].let { firebaseViewModel.loadAllAds(it.time) }*/
                        getAdsFromCat(adsList)
                    }
                }
            }
        })
    }

    private fun getAdsFromCat(adsList: ArrayList<AdData>){
        adsList[0].let {
            if (currentCategory == getString(R.string.main_ads_screen)) {
                firebaseViewModel.loadAllAdsNextPage(it.time)
            } else {
                val catTime = "${it.category}_${it.time}"
                firebaseViewModel.loadAllAdsFromCatNextPage(catTime)
            }
        }
    }

    //end
    companion object{
        const val EDIT_STATE = "edit_state"
        const val ADS_DATA = "ads_data"
        const val SCROLL_DOWN = 1
    }

}