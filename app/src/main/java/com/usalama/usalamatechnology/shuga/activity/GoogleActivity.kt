package com.usalama.usalamatechnology.shuga.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.usalama.usalamatechnology.shuga.R
import com.usalama.usalamatechnology.shuga.databinding.ActivityGoogleBinding

class GoogleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGoogleBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private companion object {
        private const val RC_SIGN_IN= 100
        private const val TAG= "GOOGLE_SIGN_IN_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoogleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val googleSignInOptions= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        firebaseAuth= FirebaseAuth.getInstance()
        checkUser()
        binding.googleSignInButton.setOnClickListener {
            Log.d(TAG, "onCreate: begin Google SIgnIn")
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }
    }
    private fun checkUser() {
        val firebaseUser= firebaseAuth.currentUser
        if (firebaseUser!=null){
            if (firebaseUser!=null){
                val intent= Intent(this, DashboardActivity::class.java)
                intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode== RC_SIGN_IN){
            Log.d(TAG, "onActivityResult: Google SignIn intent result")
            val accountTask= GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account=accountTask.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogleAccount(account.idToken!!)
            }catch (e:Exception){
                Log.d(TAG, "onActivityResult: $e")
            }
        }
    }

    private fun firebaseAuthWithGoogleAccount(idToken: String) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account")
        val credential= GoogleAuthProvider.getCredential(idToken,null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {authResult->
                Log.d(
                    TAG,
                    "firebaseAuthWithGoogleAccount: Logged In"
                )
                val firebaseUser=firebaseAuth.currentUser
                val uid = firebaseUser?.uid
                val email = firebaseUser?.email

                Log.d(TAG, "firebaseAuthWithGoogleAccount: Uid:$uid")
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Email: $email")

                if (authResult.additionalUserInfo!!.isNewUser){
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Account Created... \n")
                    Toast.makeText(this@GoogleActivity, "LoggedIn...\n$email", Toast.LENGTH_SHORT).show()
                }
                val intent= Intent(this, SignUpAs::class.java)
                intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }.addOnFailureListener {
                    e->
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Login Failed Due to ${e.message}")
                Toast.makeText(this@GoogleActivity, "Login Failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}