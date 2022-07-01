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
import com.kmv.myapplication.constants.GoogleAccConsts
import java.lang.Exception

class AccountAuthentication(act: MainActivity) {
    private val act = act
    private lateinit var signInClient: GoogleSignInClient

    fun signUpWithEmail(userEmail: String, userPsswd: String) {
        if (userEmail.isNotEmpty() && userPsswd.isNotEmpty()) {
            act.mainAuth.currentUser?.delete()?.addOnCompleteListener {tsk ->
                if (tsk.isSuccessful){
                    act.mainAuth.createUserWithEmailAndPassword(userEmail, userPsswd)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                signUpWithEmailSuccessful(task.result.user!!)
                            } else {
                                signUpWithEmailExceptions(task.exception!!, userEmail, userPsswd)

                            }
                        }
                }
            }
           /* act.mainAuth.createUserWithEmailAndPassword(userEmail, userPsswd)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        *//*sendEmailVerification(task.result.user!!)
                        act.uiUpdate(task.result.user)*//*
                        signUpWithEmailSuccessful(task.result.user!!)
                    } else {
                        signUpWithEmailExceptions(task.exception!!, userEmail, userPsswd)
                        *//*Toast.makeText(act, act.resources.getString(R.string.sign_up_error), Toast.LENGTH_LONG).show()*//*
                        *//*if (task.exception is FirebaseAuthUserCollisionException) {
                            val exception = task.exception as FirebaseAuthUserCollisionException
                            if (exception.errorCode == FirebaseAuthConstants.ERROR_EMAIL_ALREADY_IN_USE) {
                                *//**//*Toast.makeText(
                                    act,
                                    FirebaseAuthConstants.ERROR_EMAIL_ALREADY_IN_USE,
                                    Toast.LENGTH_LONG
                                ).show()*//**//*
                                //Connect email and google acc
                                connectEmailGoogleAcc(userEmail, userPsswd)
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
                        if (task.exception is FirebaseAuthWeakPasswordException) {
                            val exception =
                                task.exception as FirebaseAuthWeakPasswordException
                            if (exception.errorCode == FirebaseAuthConstants.ERROR_WEAK_PASSWORD) {
                                Toast.makeText(
                                    act,
                                    FirebaseAuthConstants.ERROR_WEAK_PASSWORD,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }*//*
                    }
                }*/
        }
    }

    private fun signUpWithEmailSuccessful(user: FirebaseUser){
        sendEmailVerification(user)
        act.uiUpdate(user)
    }

    private fun signUpWithEmailExceptions(excptn: Exception, userEmail: String, userPsswd: String){
        if (excptn is FirebaseAuthUserCollisionException) {
            if (excptn.errorCode == FirebaseAuthConstants.ERROR_EMAIL_ALREADY_IN_USE) {
                connectEmailGoogleAcc(userEmail, userPsswd)
            }
        } else if (excptn is FirebaseAuthInvalidCredentialsException) {
            if (excptn.errorCode == FirebaseAuthConstants.ERROR_INVALID_EMAIL) {
                Toast.makeText(
                    act,
                    FirebaseAuthConstants.ERROR_INVALID_EMAIL,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        if (excptn is FirebaseAuthWeakPasswordException) {
            if (excptn.errorCode == FirebaseAuthConstants.ERROR_WEAK_PASSWORD) {
                Toast.makeText(
                    act,
                    FirebaseAuthConstants.ERROR_WEAK_PASSWORD,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun signInWithEmail(userEmail: String, userPsswd: String) {
        if (userEmail.isNotEmpty() && userPsswd.isNotEmpty()) {
            act.mainAuth.currentUser?.delete()?.addOnCompleteListener {tsk ->
                if (tsk.isSuccessful){
                    act.mainAuth.signInWithEmailAndPassword(userEmail, userPsswd)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                act.uiUpdate(task.result.user)
                            } else {
                                signInWithEmailExceptions(task.exception!!, userEmail, userPsswd)
                            }
                }
            }
                /*act.mainAuth.signInWithEmailAndPassword(userEmail, userPsswd)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            act.uiUpdate(task.result.user)
                        } else {
                            signInWithEmailExceptions(task.exception!!, userEmail, userPsswd)
                            if (task.exception is FirebaseAuthInvalidCredentialsException) {
                                Log.d("MyLog", "Exception : ${task.exception}")
                                val exception =
                                    task.exception as FirebaseAuthInvalidCredentialsException
                                if (exception.errorCode == FirebaseAuthConstants.ERROR_INVALID_EMAIL) {
                                    Toast.makeText(
                                        act,
                                        FirebaseAuthConstants.ERROR_INVALID_EMAIL,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }else if (exception.errorCode == FirebaseAuthConstants.ERROR_WRONG_PASSWORD) {
                                    Toast.makeText(
                                        act,
                                        FirebaseAuthConstants.ERROR_WRONG_PASSWORD,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }else if (task.exception is FirebaseAuthInvalidUserException) {
                                    val exception = task.exception as FirebaseAuthInvalidUserException
                                    if (exception.errorCode == FirebaseAuthConstants.ERROR_USER_NOT_FOUND){
                                        Toast.makeText(
                                            act,
                                            FirebaseAuthConstants.ERROR_USER_NOT_FOUND,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }                                }
                            }
                    }*/
                }
        }
    }

    private fun signInWithEmailExceptions(exception: Exception, userEmail: String, userPsswd: String){
        if (exception is FirebaseAuthInvalidCredentialsException) {
            //Log.d("MyLog", "Exception : $exception")
            if (exception.errorCode == FirebaseAuthConstants.ERROR_INVALID_EMAIL) {
                Toast.makeText(
                    act,
                    FirebaseAuthConstants.ERROR_INVALID_EMAIL,
                    Toast.LENGTH_LONG
                ).show()
            }else if (exception.errorCode == FirebaseAuthConstants.ERROR_WRONG_PASSWORD) {
                Toast.makeText(
                    act,
                    FirebaseAuthConstants.ERROR_WRONG_PASSWORD,
                    Toast.LENGTH_LONG
                ).show()
            }else if (exception.errorCode == FirebaseAuthConstants.ERROR_USER_NOT_FOUND) {
                    Toast.makeText(
                        act,
                        FirebaseAuthConstants.ERROR_USER_NOT_FOUND,
                        Toast.LENGTH_LONG
                    ).show()                                                }
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

    fun singOutGoogle() {
        getSignInClient().signOut()
    }

    fun signInFirebaseWithGoogle(token: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        act.mainAuth.currentUser?.delete()?.addOnCompleteListener { task->
            if (task.isSuccessful){
                act.mainAuth.signInWithCredential(credential).addOnCompleteListener { task2 ->
                    if (task2.isSuccessful) {
                        Toast.makeText(act, "Sign in done", Toast.LENGTH_LONG).show()
                        act.uiUpdate(task2.result?.user)
                    }else{
                        Log.d("MyLog", "Google Sign in Exception error : ${task2.exception}")
                    }
                }
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

    fun signInAnonym(listener: Listener){
        act.mainAuth.signInAnonymously().addOnCompleteListener {task->
            if (task.isSuccessful){
                listener.onComplete()
            }else{

            }
        }
    }

    private fun connectEmailGoogleAcc(email:String, password:String){
        val credential = EmailAuthProvider.getCredential(email, password)
        if (act.mainAuth.currentUser != null){
        act.mainAuth.currentUser?.linkWithCredential(credential)?.addOnCompleteListener { task->
            if (task.isSuccessful){
                Toast.makeText(act, act.resources.getString(R.string.connect_done), Toast.LENGTH_LONG).show()
            }
        }
    }else{
            Toast.makeText(act, act.resources.getString(R.string.you_need_signin_google), Toast.LENGTH_LONG).show()
        }
    }

    interface Listener{
        fun onComplete()
    }

}