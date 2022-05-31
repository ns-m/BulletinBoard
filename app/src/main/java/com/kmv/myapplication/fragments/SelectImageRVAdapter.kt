package com.kmv.myapplication.fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kmv.myapplication.R
import com.kmv.myapplication.utils.ItemTouchMoveCallback

class SelectImageRVAdapter: RecyclerView.Adapter<SelectImageRVAdapter.ImageHolder>(), ItemTouchMoveCallback.ItemTouchAdapter {
    val mainArray = ArrayList<SelectImageItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_image_fragment_item, parent, false)
        return ImageHolder(view)
    }
    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.setData(mainArray[position])
    }
    override fun getItemCount(): Int {
        return mainArray.size
    }
    override fun onMove(startPosition: Int, targetPosition: Int) {
        val targetItem = mainArray[targetPosition]
        mainArray[targetPosition].imageUri = mainArray[startPosition].imageUri
        /*val titleStart = mainArray[targetPosition].title
        mainArray[targetPosition].title = targetItem.title*/
        mainArray[startPosition].imageUri = targetItem.imageUri
        /*mainArray[startPosition].title = titleStart*/
        notifyItemMoved(startPosition, targetPosition)
    }

    override fun onClear() {
        notifyDataSetChanged()
    }

    class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var txVTitle : TextView
        lateinit var viewImage : ImageView
        fun setData(item: SelectImageItem){
            txVTitle = itemView.findViewById(R.id.textViewTitle)
            viewImage = itemView.findViewById(R.id.imageViewContent)
            txVTitle.text = item.title
            viewImage.setImageURI(Uri.parse(item.imageUri))
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(newList: List<SelectImageItem>){
        mainArray.clear()
        mainArray.addAll(newList)
        notifyDataSetChanged()
    }

}