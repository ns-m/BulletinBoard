package com.kmv.myapplication.dialogs_support

import android.app.Activity
import android.app.AlertDialog
import com.kmv.myapplication.databinding.ProgressDialogLayoutBinding

object ProgressDialog {

    fun createProgressDialog(act: Activity): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(act)
        val binding = ProgressDialogLayoutBinding.inflate(act.layoutInflater)
        val view = binding.root
        val dialogProgr = dialogBuilder.create()
        dialogBuilder.setView(binding.root)

        dialogProgr.setCancelable(false)
        dialogBuilder.show()

        return dialogProgr
    }
}