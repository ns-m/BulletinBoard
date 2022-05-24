package com.kmv.myapplication.dialogs_support

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kmv.myapplication.R

class RCViewDialogSpinnerAdapter : RecyclerView.Adapter<RCViewDialogSpinnerAdapter.SpinnerViewHolder>() {
    private val mainList = ArrayList<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpinnerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.spinner_list_item, parent, false)
        return SpinnerViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpinnerViewHolder, position: Int) {
        holder.setData(mainList[position])
    }

    override fun getItemCount(): Int {
        return mainList.size
    }
    class SpinnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(text : String){
            val tvSpItem = itemView.findViewById<TextView>(R.id.textViewSpinnerList)
            tvSpItem.text = text
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(list: ArrayList<String>){
        mainList.clear()
        mainList.addAll(list)
        notifyDataSetChanged()
    }
}