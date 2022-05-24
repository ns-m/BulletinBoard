package com.kmv.myapplication.dialogs_support

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.kmv.myapplication.R

class DialogSpinner {
    fun showSpinnerDialog(context: Context, list:ArrayList<String>){
        val builder = AlertDialog.Builder(context)
        val binding = LayoutInflater.from(context).inflate(R.layout.spinner_layout, null)
        builder.setView(binding)
        builder.show()
    }
}