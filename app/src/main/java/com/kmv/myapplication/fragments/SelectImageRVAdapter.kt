package com.kmv.myapplication.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kmv.myapplication.R
import com.kmv.myapplication.act.EditAdsAct
import com.kmv.myapplication.databinding.SelectImageFragmentItemBinding
import com.kmv.myapplication.utils.AdapterCallback
import com.kmv.myapplication.utils.ImageManager
import com.kmv.myapplication.utils.ImagePicker
import com.kmv.myapplication.utils.ItemTouchMoveCallback

class SelectImageRVAdapter(val adapterCallback: AdapterCallback): RecyclerView.Adapter<SelectImageRVAdapter.ImageHolder>(), ItemTouchMoveCallback.ItemTouchAdapter {
    val mainArray = ArrayList<Bitmap>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val viewBinding = SelectImageFragmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageHolder(viewBinding, parent.context, this)
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

    class ImageHolder(private val viewBinding: SelectImageFragmentItemBinding, val context: Context, val adapter: SelectImageRVAdapter) : RecyclerView.ViewHolder(viewBinding.root) {
        /*lateinit var txVTitle : TextView
        lateinit var viewImage : ImageView
        lateinit var editImage : ImageButton
        lateinit var delImage : ImageButton*/

        fun setData(bitmap: Bitmap){
            /*txVTitle = itemView.findViewById(R.id.textViewTitle)
            viewImage = itemView.findViewById(R.id.imageViewContent)
            editImage = itemView.findViewById(R.id.bttnEditImage)
            delImage = itemView.findViewById(R.id.bttnImageDel)*/

            viewBinding.bttnEditImage.setOnClickListener {
                /*ImagePicker.getImages(context as EditAdsAct, 1, ImagePicker.REQUEST_CODE_EDIT_IMAGE)
                context.editImagePositions = adapterPosition*/
                /*ImagePicker.getMultiImages(context as EditAdsAct, *//*context.launcherForSingleSelectImage,*//* 1)*/
                ImagePicker.getSingleImage(context as EditAdsAct)
                context.editImagePositions = adapterPosition
            }

            viewBinding.bttnImageDel.setOnClickListener {
                adapter.mainArray.removeAt(adapterPosition)
                adapter.notifyItemRemoved(adapterPosition)
                for (n in 0 until adapter.mainArray.size) adapter.notifyItemChanged(n)
                adapter.adapterCallback.onItemDelete()
            }

            viewBinding.textViewTitle.text = context.resources.getStringArray(R.array.title_array)[adapterPosition]
            ImageManager.chooseScaleType(viewBinding.imageViewContent, bitmap)
            viewBinding.imageViewContent.setImageBitmap(bitmap)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(newList: List<Bitmap>, needClear : Boolean){
        if (needClear)mainArray.clear()
        mainArray.addAll(newList)
        notifyDataSetChanged()
    }

}