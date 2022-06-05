package com.kmv.myapplication.dialogs_support

import android.app.Activity
import android.app.AlertDialog
import com.kmv.myapplication.databinding.SignDialogBinding

object ProgressDialog {

    fun createSingDialog(act: Activity) {
        val dialogBuilder = AlertDialog.Builder(act)
        val binding = SignDialogBinding.inflate(act.layoutInflater)
        val view = binding.root
        val dialogCrt = dialogBuilder.create()
        dialogBuilder.setView(binding.root)

        dialogCrt.setCancelable(false)
        dialogBuilder.show()
    }
}