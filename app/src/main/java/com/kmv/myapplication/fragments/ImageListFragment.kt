package com.kmv.myapplication.fragments

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.get
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.kmv.myapplication.R
import com.kmv.myapplication.act.EditAdsAct
import com.kmv.myapplication.adapters.SelectImageRVAdapter
import com.kmv.myapplication.databinding.ListImageFragmentBinding
import com.kmv.myapplication.dialogs_support.ProgressDialog
import com.kmv.myapplication.utils.AdapterCallback
import com.kmv.myapplication.utils.ImageManager
import com.kmv.myapplication.utils.ImagePicker
import com.kmv.myapplication.utils.ItemTouchMoveCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ImageListFragment(private val fragmentCloseIntrf: FragmentCloseInterface/*,
                       private val newList: ArrayList<Uri>?*/) :  BaseAdsFragment(), AdapterCallback{
    /*lateinit var binding: ListImageFragmentBinding*/
    val adapter = SelectImageRVAdapter(this)
    val dragCallback = ItemTouchMoveCallback(adapter)
    val touchHelper = ItemTouchHelper(dragCallback)
    private var job: Job? = null
    private var addImageItem: MenuItem? = null
    lateinit var binding: ListImageFragmentBinding

    /*override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        *//*return inflater.inflate(R.layout.list_image_fragment, container, false)*//*
        binding = ListImageFragmentBinding.inflate(inflater)
        return binding.root
    }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ListImageFragmentBinding.inflate(layoutInflater)
        adView = binding.adView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        /*val bttnBack = view.findViewById<Button>(R.id.bttnBack)*/
        /*val rcView = view.findViewById<RecyclerView>(R.id.recyclerViewSelectImage)*/
        /*touchHelper.attachToRecyclerView(rcView)
        rcView.layoutManager = LinearLayoutManager(activity)
        rcView.adapter = adapter*/
        binding.apply {
        touchHelper.attachToRecyclerView(recyclerViewSelectImage)
        recyclerViewSelectImage.layoutManager = LinearLayoutManager(activity)
        recyclerViewSelectImage.adapter = adapter
        /*val updateList = ArrayList<SelectImageItem>()
        for (n in 0 until newList.size){
            updateList.add(SelectImageItem(n.toString(), newList[n]))
        }*/
        /*if (newList != null) resizeSelectedImages(newList, true)*/
        }
        /*bttnBack.setOnClickListener {*/
        //activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        /*}*/
    }

    override fun onItemDelete() {
        addImageItem?.isVisible = true
    }

    fun updateAdapterFromEdit(bitmapList: List<Bitmap>){
        adapter.updateAdapter(bitmapList, true)
    }

    override fun onDetach() {
        super.onDetach()
        fragmentCloseIntrf.onFragmentClose(adapter.mainArray)
        job?.cancel()
    }

    override fun onClose() {
        super.onClose()
        activity?.supportFragmentManager?.beginTransaction()?.remove(this@ImageListFragment)?.commit()
    }

    fun resizeSelectedImages(newList: ArrayList<Uri>, needClear: Boolean, activity: Activity){

        job = CoroutineScope(Dispatchers.Main).launch {
            val dialog = ProgressDialog.createProgressDialog(activity)
            val bitmapList = ImageManager.imageResize(newList, activity)
            dialog.dismiss()
            adapter.updateAdapter(bitmapList, needClear)
            if (adapter.mainArray.size > ImagePicker.MAX_IMAGE_COUNT - 1) addImageItem?.isVisible = false
        }

    }

    private fun setUpToolbar(){

        binding.apply {
        toolbarImageFragment.inflateMenu(R.menu.menu_choose_image)
        val deleteItem = toolbarImageFragment.menu.findItem(R.id.image_delete_button)
        addImageItem = toolbarImageFragment.menu.findItem(R.id.image_add_button)
        if (adapter.mainArray.size > ImagePicker.MAX_IMAGE_COUNT - 1) addImageItem?.isVisible = false

        toolbarImageFragment.setNavigationOnClickListener {
            showInterAd()
        }

        deleteItem.setOnMenuItemClickListener{
            adapter.updateAdapter(ArrayList(), true)
            addImageItem?.isVisible = true
            true
        }

        addImageItem?.setOnMenuItemClickListener{
            val imageCount = ImagePicker.MAX_IMAGE_COUNT - adapter.mainArray.size
            /*ImagePicker.getImages(activity as AppCompatActivity, imageCount, ImagePicker.REQUEST_CODE_GET_IMAGE)*/
            /*ImagePicker.getMultiImages(activity as EditAdsAct,*//*(activity as EditAdsAct).launcherMultiSelectImages,*//* imageCount)*/
            ImagePicker.addImages(activity as EditAdsAct, imageCount)
            true
        }
        }
    }

    fun updateAdapter(newList: ArrayList<Uri>, activity: Activity){
        /*val updateList = ArrayList<SelectImageItem>()
        for (n in adapter.mainArray.size until newList.size + adapter.mainArray.size){
            updateList.add(SelectImageItem(n.toString(), newList[n - adapter.mainArray.size]))
        }*/
        /*job = CoroutineScope(Dispatchers.Main).launch {
            val bitmapList = ImageManager.imageResize(newList)
            adapter.updateAdapter(bitmapList, false)
        }*/
        //adapter.updateAdapter(newList, false)
        resizeSelectedImages(newList, false, activity)
    }

    fun setSingleImage(uri : Uri, position : Int){
        val progrBar = binding.recyclerViewSelectImage[position].findViewById<ProgressBar>(R.id.progrBarSingleimage)

        job = CoroutineScope(Dispatchers.Main).launch {
            progrBar.visibility = View.VISIBLE
            val bitmapList = ImageManager.imageResize(arrayListOf(uri), activity as Activity)
            //adapter.updateAdapter(bitmapList, false)
            progrBar.visibility = View.GONE
            adapter.mainArray[position] = bitmapList[0]
            adapter.notifyItemChanged(position)
        }
    }

}