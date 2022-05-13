package com.kmv.myapplication.dialogs_support

import android.app.AlertDialog
import android.widget.Toast
import com.kmv.myapplication.MainActivity
import com.kmv.myapplication.R
import com.kmv.myapplication.authentication.AccountAuthentication
import com.kmv.myapplication.databinding.SignDialogBinding

class DialogSupport(act:MainActivity) {
    private val act = act
    private val accAuth = AccountAuthentication(act)
    fun createSingDialog(intCode:Int){
        val dialogBuilder = AlertDialog.Builder(act)
        val binding = SignDialogBinding.inflate(act.layoutInflater)
        dialogBuilder.setView(binding.root)
        if (intCode == DialogConsts.SIGN_UP_STATE){
            binding.tViewSignTitle.text = act.resources.getString(R.string.app_set_ac_sign_up)
            binding.bttnSignUpSignIn.text = act.resources.getString(R.string.app_sign_up_action)
        }else{
            binding.tViewSignTitle.text = act.resources.getString(R.string.app_set_ac_sign_in)
            binding.bttnSignUpSignIn.text = act.resources.getString(R.string.app_sign_in_action)
        }
        val dialogCrt = dialogBuilder.create()
        binding.bttnSignUpSignIn.setOnClickListener {
            dialogCrt.dismiss()
            if (intCode == DialogConsts.SIGN_UP_STATE){
                accAuth.signUpWithEmail(binding.editTxSignEmail.text.toString(),
                    binding.editTxSignPassword.text.toString())
            }else{
                accAuth.signInWithEmail(binding.editTxSignEmail.text.toString(),
                    binding.editTxSignPassword.text.toString())
                Toast.makeText(this, R.string.app_login_accept, Toast.LENGTH_LONG).show()
            }
        }
        dialogBuilder.show()
    }
}