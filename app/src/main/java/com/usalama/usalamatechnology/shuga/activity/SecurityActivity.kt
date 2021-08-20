package com.usalama.usalamatechnology.shuga.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.usalama.usalamatechnology.shuga.BaseActivity
import com.usalama.usalamatechnology.shuga.databinding.ActivitySecurityBinding
import com.usalama.usalamatechnology.shuga.models.Security
import com.usalama.usalamatechnology.shuga.models.User

class SecurityActivity : BaseActivity() {
    private lateinit var binding: ActivitySecurityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecurityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.setBtn.setOnClickListener {
            saveProfileToFirebase()
        }
    }

    private fun saveProfileToFirebase() {
        val uid = FirebaseAuth.getInstance().uid?:""
        val username=binding.username.text.toString()
        //val email=binding.email.text.toString()
        val password =binding.password.text.toString()

        if (username.isNotEmpty()||password.isNotEmpty()){
            val ref = FirebaseDatabase.getInstance().getReference("/users/security/$username")
            val user= Security(uid,username,password)

            ref.setValue(user)
                .addOnSuccessListener {
                    Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show()
                    val intent= Intent(this, DashboardActivity::class.java)
                    intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Log.d("fail","Failed ${it.message}")
                }
        }else{
            Toast.makeText(this, "Empty Fields", Toast.LENGTH_SHORT).show()
        }
    }
}