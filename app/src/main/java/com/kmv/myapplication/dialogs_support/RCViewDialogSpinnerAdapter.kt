package com.kmv.myapplication.dialogs_support

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kmv.myapplication.R
import com.kmv.myapplication.act.EditAdsAct

class RCViewDialogSpinnerAdapter(var context: Context, var dialog: AlertDialog) : RecyclerView.Adapter<RCViewDialogSpinnerAdapter.SpinnerViewHolder>() {
    private val mainList = ArrayList<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpinnerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.spinner_list_item, parent, false)
        return SpinnerViewHolder(view, context, dialog)
    }

    override fun onBindViewHolder(holder: SpinnerViewHolder, position: Int) {
        holder.setData(mainList[position])
    }

    override fun getItemCount(): Int {
        return mainList.size
    }
    class SpinnerViewHolder(itemView: View, var context: Context, var dialog: AlertDialog) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var itemText = ""
        fun setData(text : String){
            val tvSpItem = itemView.findViewById<TextView>(R.id.textViewSpinnerList)
            tvSpItem.text = text
            itemText = text
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            (context as EditAdsAct).binding.textViewSelectCountry.text = itemText
            dialog.dismiss()
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(list: ArrayList<String>){
        mainList.clear()
        mainList.addAll(list)
        notifyDataSetChanged()
    }
}