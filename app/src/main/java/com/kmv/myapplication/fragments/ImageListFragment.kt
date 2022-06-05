package com.kmv.myapplication.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kmv.myapplication.R
import com.kmv.myapplication.databinding.ListImageFragmentBinding
import com.kmv.myapplication.utils.ImageManager
import com.kmv.myapplication.utils.ImagePicker
import com.kmv.myapplication.utils.ItemTouchMoveCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ImageListFragment(private val fragmentCloseIntrf: FragmentCloseInterface,
                       private val newList: ArrayList<String>?) :  Fragment(){
    lateinit var binding: ListImageFragmentBinding
    val adapter = SelectImageRVAdapter()
    val dragCallback = ItemTouchMoveCallback(adapter)
    val touchHelper = ItemTouchHelper(dragCallback)
    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        /*return inflater.inflate(R.layout.list_image_fragment, container, false)*/
        binding = ListImageFragmentBinding.inflate(inflater)
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
        touchHelper.attachToRecyclerView(binding.recyclerViewSelectImage)
        binding.recyclerViewSelectImage.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewSelectImage.adapter = adapter
        /*val updateList = ArrayList<SelectImageItem>()
        for (n in 0 until newList.size){
            updateList.add(SelectImageItem(n.toString(), newList[n]))
        }*/

        if (newList != null) {
            job = CoroutineScope(Dispatchers.Main).launch {
                val bitmapList = ImageManager.imageResize(newList)
                adapter.updateAdapter(bitmapList, true)
            }
        }

        /*bttnBack.setOnClickListener {*/
        //activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        /*}*/
    }

    fun updateAdapterFromEdit(bitmapList: List<Bitmap>){
        adapter.updateAdapter(bitmapList, true)
    }

    override fun onDetach() {
        super.onDetach()
        fragmentCloseIntrf.onFragmentClose(adapter.mainArray)
        job?.cancel()
    }

    private fun setUpToolbar(){
        binding.toolbarImageFragment.inflateMenu(R.menu.menu_choose_image)
        val deleteItem = binding.toolbarImageFragment.menu.findItem(R.id.image_delete_button)
        val addImageItem = binding.toolbarImageFragment.menu.findItem(R.id.image_add_button)

        binding.toolbarImageFragment.setNavigationOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
        deleteItem.setOnMenuItemClickListener{
            adapter.updateAdapter(ArrayList(), true)
            true
        }
        addImageItem.setOnMenuItemClickListener{
            val imageCount = ImagePicker.MAX_IMAGE_COUNT - adapter.mainArray.size
            ImagePicker.getImages(activity as AppCompatActivity, imageCount, ImagePicker.REQUEST_CODE_GET_IMAGE)
            true
        }
    }

    fun updateAdapter(newList: ArrayList<String>){
        /*val updateList = ArrayList<SelectImageItem>()
        for (n in adapter.mainArray.size until newList.size + adapter.mainArray.size){
            updateList.add(SelectImageItem(n.toString(), newList[n - adapter.mainArray.size]))
        }*/
        job = CoroutineScope(Dispatchers.Main).launch {
            val bitmapList = ImageManager.imageResize(newList)
            adapter.updateAdapter(bitmapList, false)
        }
        //adapter.updateAdapter(newList, false)
    }

    fun setSingleImage(uri : String, position : Int){
        job = CoroutineScope(Dispatchers.Main).launch {
            val bitmapList = ImageManager.imageResize(listOf(uri))
            //adapter.updateAdapter(bitmapList, false)
            adapter.mainArray[position] = bitmapList[0]
            adapter.notifyDataSetChanged()
        }
    }
}