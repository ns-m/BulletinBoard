package com.kmv.myapplication.dialogs_support

import android.app.AlertDialog
import android.view.View
import android.widget.Toast
import com.kmv.myapplication.MainActivity
import com.kmv.myapplication.R
import com.kmv.myapplication.authentication.AccountAuthentication
import com.kmv.myapplication.databinding.SignDialogBinding

class DialogSupport(act: MainActivity) {
    private val act = act
    val accAuth = AccountAuthentication(act)

    fun createSingDialog(intCode: Int) {
        val dialogBuilder = AlertDialog.Builder(act)
        val binding = SignDialogBinding.inflate(act.layoutInflater)
        dialogBuilder.setView(binding.root)
        setDialogState(intCode, binding)
        val dialogCrt = dialogBuilder.create()
        binding.bttnSignUpSignIn.setOnClickListener {
            setOnClickSignUpIn(intCode, binding, dialogCrt)
        }
        binding.bttnFogotRsswrd.setOnClickListener {
            setOnClickResetPsswrd(binding, dialogCrt)
        }
        binding.bttnSignInGoogle.setOnClickListener {
            accAuth.singInWithGoogle()
            dialogCrt.dismiss()
        }
        dialogBuilder.show()
    }

    private fun setOnClickResetPsswrd(binding: SignDialogBinding, dialogCrt: AlertDialog?) {
        if (binding.editTxSignEmail.text.isNotEmpty()) {
            act.mainAuth.sendPasswordResetEmail(binding.editTxSignEmail.text.toString())
                .addOnCompleteListener { tast ->
                    if (tast.isSuccessful) {
                        Toast.makeText(
                            act,
                            R.string.sign_email_reset_psswrd_send,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            dialogCrt?.dismiss()
        } else {
            binding.txViewDialogMssg.visibility = View.VISIBLE
        }
    }

    private fun setOnClickSignUpIn(
        intCode: Int,
        binding: SignDialogBinding,
        dialogCrt: AlertDialog?
    ) {
        dialogCrt?.dismiss()
        if (intCode == DialogConsts.SIGN_UP_STATE) {
            accAuth.signUpWithEmail(
                binding.editTxSignEmail.text.toString(),
                binding.editTxSignPassword.text.toString()
            )
        } else {
            accAuth.signInWithEmail(
                binding.editTxSignEmail.text.toString(),
                binding.editTxSignPassword.text.toString()
            )
            /*Toast.makeText(this, R.string.app_login_accept, Toast.LENGTH_LONG).show()*/
        }
    }

    private fun setDialogState(intCode: Int, binding: SignDialogBinding) {
        if (intCode == DialogConsts.SIGN_UP_STATE) {
            binding.tViewSignTitle.text = act.resources.getString(R.string.set_ac_sign_up)
            binding.bttnSignUpSignIn.text = act.resources.getString(R.string.sign_up_action)
        } else {
            binding.tViewSignTitle.text = act.resources.getString(R.string.set_ac_sign_in)
            binding.bttnSignUpSignIn.text = act.resources.getString(R.string.sign_in_action)
            binding.bttnFogotRsswrd.visibility = View.VISIBLE
        }
    }
}