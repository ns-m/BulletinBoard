package com.kmv.myapplication.dialogs_support

import android.app.AlertDialog
import com.kmv.myapplication.MainActivity
import com.kmv.myapplication.R
import com.kmv.myapplication.databinding.SignDialogBinding

class DialogSupport(act:MainActivity) {
    private val act = act
    fun createSingDialog(intCode:Int){
        val dialogBuilder = AlertDialog.Builder(act)
        val binding = SignDialogBinding.inflate(act.layoutInflater)
        if (intCode == DialogConsts.SIGN_UP_STATE){
            binding.tViewSignTitle.text = act.resources.getString(R.string.app_set_ac_sign_up)
            binding.bttnSignUpSignIn.text = act.resources.getString(R.string.app_sign_up_action)
        }else{
            binding.tViewSignTitle.text = act.resources.getString(R.string.app_set_ac_sign_in)
            binding.bttnSignUpSignIn.text = act.resources.getString(R.string.app_sign_in_action)
        }
        dialogBuilder.setView(binding.root)
        dialogBuilder.show()
    }
}