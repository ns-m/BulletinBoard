package com.kmv.myapplication.fragments

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
import com.kmv.myapplication.utils.ImagePicker
import com.kmv.myapplication.utils.ItemTouchMoveCallback

class ImageListFragment(private val fragmentCloseIntrf: FragmentCloseInterface,
                       private val newList: ArrayList<String>) :  Fragment(){
    lateinit var binding : ListImageFragmentBinding
    val adapter = SelectImageRVAdapter()
    val dragCallback = ItemTouchMoveCallback(adapter)
    val touchHelper = ItemTouchHelper(dragCallback)
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
        val updateList = ArrayList<SelectImageItem>()
        for (n in 0 until newList.size){
            updateList.add(SelectImageItem(n.toString(), newList[n]))
        }
        adapter.updateAdapter(updateList, true)
        /*bttnBack.setOnClickListener {*/
        //activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        /*}*/
    }

    override fun onDetach() {
        super.onDetach()
        fragmentCloseIntrf.onFragmentClose(adapter.mainArray)
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
            ImagePicker.getImages(activity as AppCompatActivity, imageCount)
            true
        }
    }
    fun updateAdapter(newList: ArrayList<String>){
        val updateList = ArrayList<SelectImageItem>()
        for (n in adapter.mainArray.size until newList.size){
            updateList.add(SelectImageItem(n.toString(), newList[n]))
        }
        adapter.updateAdapter(updateList, false)
    }
}