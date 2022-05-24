package com.kmv.myapplication.dialogs_support

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kmv.myapplication.R

class DialogSpinner {
    fun showSpinnerDialog(context: Context, list:ArrayList<String>){
        val builder = AlertDialog.Builder(context)
        val binding = LayoutInflater.from(context).inflate(R.layout.spinner_layout, null)
        val adapter = RCViewDialogSpinnerAdapter()
        val rcView = binding.findViewById<RecyclerView>(R.id.spinnerRecyclerView)
        rcView.layoutManager = LinearLayoutManager(context)
        rcView.adapter = adapter
        adapter.updateAdapter(list)
        builder.setView(binding)
        builder.show()
    }
}