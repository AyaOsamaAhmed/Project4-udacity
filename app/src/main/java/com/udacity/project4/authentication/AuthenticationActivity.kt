package com.udacity.project4.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.udacity.project4.R
import com.udacity.project4.databinding.ActivityAuthenticationBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.locationreminders.RemindersActivity

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {

    companion object {
        const val SIGN_IN_REQUEST_CODE = 1001
    }

    private lateinit var binding : ActivityAuthenticationBinding

    private val viewModel by viewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

//          TODO: If the user was authenticated, send him to RemindersActivity

//          TODO: a bonus is to customize the sign in flow to look nice using :
        //https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md#custom-layout

        binding =  DataBindingUtil.setContentView(this, R.layout.activity_authentication)

        viewModel.authenticationState.observe(this, Observer { state ->
            when (state) {
                //         : If the user was authenticated, send him to RemindersActivity
                AuthenticationViewModel.UserState.AUTH -> {
                    viewModel.onActive()
                    startRemindersActivity()
                }
                AuthenticationViewModel.UserState.UNAUTH ->{
                    viewModel.unActive()
                }
            }
        })

        binding.login.setOnClickListener {
            launchSignIn()
        }
    }

    //          : Implement the create account and sign in using FirebaseUI, use sign in using email and sign in using Google
    private fun launchSignIn() {

        val providers = kotlin.collections.arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(),
            com.udacity.project4.authentication.AuthenticationActivity.Companion.SIGN_IN_REQUEST_CODE
        )
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_REQUEST_CODE) {

            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                Log.i("result Login", "Successfully signed in user " + "${FirebaseAuth.getInstance().currentUser?.displayName}!")
                startRemindersActivity()
            } else {
                // Sign in failed. If response is null the user canceled the sign-in flow using
                Log.i("result Login", "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }

    private fun startRemindersActivity() {
        val intent = Intent(this, RemindersActivity::class.java)
        startActivity(intent)
    }

}
