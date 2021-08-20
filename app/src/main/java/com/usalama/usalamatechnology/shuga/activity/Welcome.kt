package com.usalama.usalamatechnology.shuga.activity

import android.os.Bundle
import com.usalama.usalamatechnology.shuga.BaseActivity
import com.usalama.usalamatechnology.shuga.databinding.ActivityWelcomeBinding
import com.usalama.usalamatechnology.shuga.utils.launchActivity
import com.usalama.usalamatechnology.shuga.utils.onClick

class Welcome : BaseActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.login.onClick { launchActivity<LoginActivity>() }
        binding.signup.onClick { launchActivity<SignUpActivity>() }
        binding.google.onClick { launchActivity<GoogleActivity>() }
        //binding.phone.onClick { launchActivity<SignUpPhoneActivity>() }
        binding.fblogin.onClick { launchActivity<FacebookActivity>() }
    }
}