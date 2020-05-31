package com.example.myapplication

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.telephony.SubscriptionManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 1001



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

// Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.mipmap.ic_launcher_round)
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                val typeOfLogin = user?.providerData?.get(1)?.providerId
                Log.e("message",typeOfLogin.toString())
                if(typeOfLogin.equals("password")) {
                    if (user != null) {
                        if(!user.isEmailVerified) {
                            user.sendEmailVerification()
                        }
                    }
                }
                startActivity(intent)
            } else {
                //TODO
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if(user != null)
        {
            val intent = Intent(this, Main2Activity::class.java)
            val typeOfLogin = user.providerData[1].providerId
            Log.e("message",typeOfLogin.toString())
            if(typeOfLogin.equals("password")) {
                if(!user.isEmailVerified) {
                    //TODO
                }
            }
            startActivity(intent)
            finishActivity(RC_SIGN_IN)
            finish()
        }
    }
}
