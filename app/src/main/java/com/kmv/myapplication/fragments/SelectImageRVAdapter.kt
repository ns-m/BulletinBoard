package com.kmv.myapplication.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kmv.myapplication.R
import com.kmv.myapplication.act.EditAdsAct
import com.kmv.myapplication.utils.ImagePicker
import com.kmv.myapplication.utils.ItemTouchMoveCallback

class SelectImageRVAdapter: RecyclerView.Adapter<SelectImageRVAdapter.ImageHolder>(), ItemTouchMoveCallback.ItemTouchAdapter {
    val mainArray = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_image_fragment_item, parent, false)
        return ImageHolder(view, parent.context, this)
    }
    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.setData(mainArray[position])
    }
    override fun getItemCount(): Int {
        return mainArray.size
    }
    override fun onMove(startPosition: Int, targetPosition: Int) {
        val targetItem = mainArray[targetPosition]
        mainArray[targetPosition] = mainArray[startPosition]
        /*val titleStart = mainArray[targetPosition].title
        mainArray[targetPosition].title = targetItem.title*/
        mainArray[startPosition] = targetItem
        /*mainArray[startPosition].title = titleStart*/
        notifyItemMoved(startPosition, targetPosition)
    }

    override fun onClear() {
        notifyDataSetChanged()
    }

    class ImageHolder(itemView: View, val context: Context, val adapter: SelectImageRVAdapter) : RecyclerView.ViewHolder(itemView) {
        lateinit var txVTitle : TextView
        lateinit var viewImage : ImageView
        lateinit var editImage : ImageButton
        lateinit var delImage : ImageButton

        fun setData(item: String){
            txVTitle = itemView.findViewById(R.id.textViewTitle)
            viewImage = itemView.findViewById(R.id.imageViewContent)
            editImage = itemView.findViewById(R.id.bttnEditImage)
            delImage = itemView.findViewById(R.id.bttnImageDel)

            editImage.setOnClickListener {
                ImagePicker.getImages(context as EditAdsAct, 1, ImagePicker.REQUEST_CODE_EDIT_IMAGE)
                context.editImagePositions = adapterPosition
            }
            delImage.setOnClickListener {
                adapter.mainArray.removeAt(adapterPosition)
                adapter.notifyItemRemoved(adapterPosition)
                for (n in 0 until adapter.mainArray.size) adapter.notifyItemChanged(n)
            }

            txVTitle.text = context.resources.getStringArray(R.array.title_array)[adapterPosition]
            viewImage.setImageURI(Uri.parse(item))
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(newList: List<String>, needClear : Boolean){
        if (needClear)mainArray.clear()
        mainArray.addAll(newList)
        notifyDataSetChanged()
    }

}