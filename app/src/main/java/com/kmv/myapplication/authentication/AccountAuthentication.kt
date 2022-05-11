package com.kmv.myapplication.authentication

import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.kmv.myapplication.MainActivity
import com.kmv.myapplication.R

class AccountAuthentication(act:MainActivity) {
    private val act = act
    fun signUpWithEmail(userEmail:String, userPsswd:String){
        if (userEmail.isNotEmpty() && userPsswd.length > 6){
            act.mainAuth.createUserWithEmailAndPassword(userEmail, userPsswd).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    sendEmailVerification(task.result.user!!)
                }else{
                    Toast.makeText(act, act.resources.getString(R.string.app_sign_up_error), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun sendEmailVerification(userFireB:FirebaseUser){
        userFireB.sendEmailVerification().addOnCompleteListener {task ->
            if (task.isSuccessful){
                Toast.makeText(act, act.resources.getString(R.string.app_sign_up_send_email), Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(act, act.resources.getString(R.string.app_sign_up_error_send_email), Toast.LENGTH_LONG).show()
            }

        }
    }
}