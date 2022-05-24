package com.kmv.myapplication.dialogs_support

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kmv.myapplication.R

class RCViewDialogSpinner : RecyclerView.Adapter<RCViewDialogSpinner.SpinnerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpinnerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.spinner_list_item, parent, false)
        return SpinnerViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpinnerViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
    class SpinnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(text : String){

        }
    }
}