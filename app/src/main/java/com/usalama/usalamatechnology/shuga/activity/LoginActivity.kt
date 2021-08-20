package com.usalama.usalamatechnology.shuga.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.usalama.usalamatechnology.shuga.BaseActivity
import com.usalama.usalamatechnology.shuga.databinding.ActivityLoginBinding
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.password

class LoginActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        register.setOnClickListener {
            startActivity(Intent (this,SignUpActivity::class.java))
            finish()
        }
        login.setOnClickListener{
            doLogin()
        }


//        setContentView(binding.root)
//        binding.login.onClick { launchActivity<DashboardActivity> { } }
//        binding.register.onClick { launchActivity<SignUpActivity> { } }
    }

    private fun doLogin() {
        if (username.text.toString().isEmpty()){
            username.error = "Enter your Username"
            username.requestFocus()
            return
        }
        if (password.text.toString().isEmpty()){
            password.error = "Enter your Password"
            password.requestFocus()
            return
        }
        val userName : String = binding.username.text.toString()
        val password : String = binding.password.text.toString()
        if  (userName.isNotEmpty()){

            readData(userName,password)

        }else{

            Toast.makeText(this,"PLease enter the Username",Toast.LENGTH_SHORT).show()

        }

    }

    private fun readData(userName: String, password: String) {
        database = FirebaseDatabase.getInstance().getReference("users/security")
        database.child(userName).get().addOnSuccessListener {

            if (it.exists()){

                val access = it.child("password").value.toString()
                if (access==password){
                    val uid = it.child("uid").value.toString()
                    Toast.makeText(this,"Successfuly Read",Toast.LENGTH_SHORT).show()
                    binding.username.text.clear()
                    binding.password.text.clear()
                    checkDetails(uid)
                }else if (access!=password){
                    binding.password.text.clear()
                    Toast.makeText(this,"Check password and try again...",Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }


            }else{

                Toast.makeText(this,"User Doesn't Exist",Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener{

            Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()


        }

    }

    private fun checkDetails(uid: String) {
        database = FirebaseDatabase.getInstance().getReference("users/shugas")
        database.child(uid).get().addOnSuccessListener {

            if (it.exists()){
                val intent= Intent(this, DashboardActivity::class.java)
                val user = auth.currentUser
                intent.putExtra("uid", uid)
                intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }else{

                Toast.makeText(this,"User Doesn't Exist",Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener{

            Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()


        }
    }

    /*private fun doLogin() {
        if (email.text.toString().isEmpty()){
            email.error = "Enter your Email"
            email.requestFocus()
            return
        }

        *//*if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            email.error = "Enter a valid email"
            email.requestFocus()
            return
        }*//*
        if (password.text.toString().isEmpty()){
            password.error = "Enter a Password"
            password.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    
                    updateUI(null)
                }
            }
    }*/
//    public override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        updateUI (currentUser)
//    }

    private fun updateUI(currentUser: FirebaseUser?){
         if(currentUser !=null) {
             if (currentUser.isEmailVerified){
                 startActivity(Intent(this, DashboardActivity::class.java))
                 finish()
             }else{
                 Toast.makeText(
                     baseContext, "please verify your email address",
                     Toast.LENGTH_SHORT
                 ).show()
             }

         } else {
             Toast.makeText(
                 baseContext, "Login Failed. Incorrect username or password",
                 Toast.LENGTH_SHORT
             ).show()
         }
    }
}