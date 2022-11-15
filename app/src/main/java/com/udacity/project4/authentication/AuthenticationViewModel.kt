package com.udacity.project4.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.firebase.auth.FirebaseAuth

class AuthenticationViewModel : ViewModel()  {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private  var firebase  = MutableLiveData<Any>()

    private val authStateListener  = FirebaseAuth.AuthStateListener { firebaseAuth ->
       firebase.value = firebaseAuth.currentUser
    }

    enum class UserState {
        AUTH, UNAUTH
    }

    val  authenticationState = firebase.map { user ->
            if (user != null) {
                UserState.AUTH
            } else {
                UserState.UNAUTH
            }
        }



    fun onActive(){
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    fun unActive(){
        firebaseAuth.removeAuthStateListener(authStateListener)
    }
}