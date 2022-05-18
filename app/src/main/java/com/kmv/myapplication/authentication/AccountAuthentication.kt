package com.kmv.myapplication.authentication

import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*
import com.kmv.myapplication.MainActivity
import com.kmv.myapplication.R
import com.kmv.myapplication.constants.FirebaseAuthConstants
import com.kmv.myapplication.dialogs_support.GoogleAccConsts

class AccountAuthentication(act: MainActivity) {
    private val act = act
    private lateinit var signInClient: GoogleSignInClient
    fun signUpWithEmail(userEmail: String, userPsswd: String) {
        if (userEmail.isNotEmpty() && userPsswd.isNotEmpty()) {
            act.mainAuth.createUserWithEmailAndPassword(userEmail, userPsswd)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sendEmailVerification(task.result.user!!)
                        act.uiUpdate(task.result.user)
                    } else {
                        /*Toast.makeText(act, act.resources.getString(R.string.sign_up_error), Toast.LENGTH_LONG).show()*/
                        if (task.exception is FirebaseAuthUserCollisionException) {
                            val exception = task.exception as FirebaseAuthUserCollisionException
                            if (exception.errorCode == FirebaseAuthConstants.ERROR_EMAIL_ALREADY_IN_USE) {
                                Toast.makeText(
                                    act,
                                    FirebaseAuthConstants.ERROR_EMAIL_ALREADY_IN_USE,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            val exception =
                                task.exception as FirebaseAuthInvalidCredentialsException
                            if (exception.errorCode == FirebaseAuthConstants.ERROR_INVALID_EMAIL) {
                                Toast.makeText(
                                    act,
                                    FirebaseAuthConstants.ERROR_INVALID_EMAIL,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
        }
    }

    fun signInWithEmail(userEmail: String, userPsswd: String) {
        if (userEmail.isNotEmpty() && userPsswd.isNotEmpty()) {
            act.mainAuth.signInWithEmailAndPassword(userEmail, userPsswd)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        act.uiUpdate(task.result.user)
                    } else {
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            val exception =
                                task.exception as FirebaseAuthInvalidCredentialsException
                            if (exception.errorCode == FirebaseAuthConstants.ERROR_INVALID_EMAIL) {
                                Toast.makeText(
                                    act,
                                    FirebaseAuthConstants.ERROR_INVALID_EMAIL,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
        }
    }

    private fun getSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(act.getString(R.string.default_web_client_id)).requestEmail().build()
        return GoogleSignIn.getClient(act, gso)
    }

    fun singInWithGoogle() {
        signInClient = getSignInClient()
        val intent = signInClient.signInIntent
        act.startActivityForResult(intent, GoogleAccConsts.SIGN_IN_REQUEST_CODE)
    }

    fun signInFirebaseWithGoogle(token: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        act.mainAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(act, "Sign in done", Toast.LENGTH_LONG).show()
                act.uiUpdate(task.result?.user)
            }else{
                Log.d("MyLog", "Google Sign in Exception error : ${task.exception}")
            }
        }
    }

    private fun sendEmailVerification(userFireB: FirebaseUser) {
        userFireB.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    act,
                    act.resources.getString(R.string.sign_up_send_email),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    act,
                    act.resources.getString(R.string.sign_up_error_send_email),
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }
}