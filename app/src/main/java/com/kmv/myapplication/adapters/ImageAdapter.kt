package com.kmv.myapplication.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.kmv.myapplication.R
import com.kmv.myapplication.fragments.SelectImageItem

class ImageAdapter: RecyclerView.Adapter<ImageAdapter.ImageHolder>() {
    val mainArray = ArrayList<SelectImageItem>()
    class ImageHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        lateinit var imView: ImageView
        fun setData(uri : String){
            imView = itemView.findViewById(R.id.imgViewItem)
            imView.setImageURI(Uri.parse(uri))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_adapter_item, parent, false)
        return ImageHolder(view)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.setData(mainArray[position].imageUri)
    }

    override fun getItemCount(): Int {
        return mainArray.size
    }
    fun update(newList : ArrayList<SelectImageItem>){
        mainArray.clear()
        mainArray.addAll(newList)
        notifyDataSetChanged()
    }
}