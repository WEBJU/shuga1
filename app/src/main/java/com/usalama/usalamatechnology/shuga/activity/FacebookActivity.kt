package com.usalama.usalamatechnology.shuga.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import com.usalama.usalamatechnology.shuga.R
import com.usalama.usalamatechnology.shuga.databinding.ActivityFacebookBinding
import kotlinx.android.synthetic.main.activity_facebook.*

class FacebookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFacebookBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var callbackManager : CallbackManager
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private lateinit var accessTokenTracker: AccessTokenTracker

    private companion object {
        private const val TAG= "FACEBOOK_IN_TAG"}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebook)
        binding= ActivityFacebookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth= FirebaseAuth.getInstance()
        checkUser()

        FacebookSdk.sdkInitialize(applicationContext)
        callbackManager = CallbackManager.Factory.create();
        binding.loginButton.setPermissions("email","public_profile")
        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
            }
        })
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val firebaseUser= firebaseAuth.currentUser
            if (firebaseUser!=null){
                updateUI(firebaseUser)
            }else{
                updateUI(null)
            }
        }
    }

    private fun checkUser() {
        val firebaseUser= firebaseAuth.currentUser

        if (firebaseUser!=null){
            val intent= Intent(this, DashboardActivity::class.java)
            intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        if (authStateListener!=null){
            firebaseAuth.addAuthStateListener(authStateListener)
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val firebaseuser = firebaseAuth.currentUser
                    updateUI(firebaseuser)
                    val intent= Intent(this, SignUpAs::class.java)

                    intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    checkUser()
                }
            }
    }

    private fun updateUI(firebaseuser: FirebaseUser?) {
        if (firebaseuser!=null){
            binding.FbName.text = firebaseuser.displayName
            if (firebaseuser.photoUrl !=null){
                var photoUrl = firebaseuser.photoUrl.toString()
                photoUrl += "?type=large"
                Picasso.get().load(photoUrl).into(fbImage)
            }
        }else{
            binding.FbName.text = ""
            fbImage.setImageResource(R.drawable.da_ic_facebook)
        }

    }
}