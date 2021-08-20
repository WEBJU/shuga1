package com.usalama.usalamatechnology.shuga.activity

import android.content.Intent
import android.os.Bundle
import com.usalama.usalamatechnology.shuga.BaseActivity
import com.usalama.usalamatechnology.shuga.databinding.ActivitySignupBinding
import com.usalama.usalamatechnology.shuga.utils.launchActivity
import com.usalama.usalamatechnology.shuga.utils.onClick

class SignUpActivity : BaseActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignupPhone.setOnClickListener {
            startActivity(
                Intent(this, SignUpPhoneActivity::class.java)
            )
        }
        binding.btnSignupGoogle.setOnClickListener {
            startActivity(
                Intent(this, GoogleActivity::class.java)
            )
        }

        binding.btnSignupFB.setOnClickListener {
            startActivity(
                Intent(this, FacebookActivity::class.java)
            )
        }
    }

}