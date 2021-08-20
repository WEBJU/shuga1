package com.usalama.usalamatechnology.shuga.activity

import android.os.Bundle
import com.usalama.usalamatechnology.shuga.BaseActivity
import com.usalama.usalamatechnology.shuga.databinding.ActivitySubscriptionBinding
import com.usalama.usalamatechnology.shuga.utils.launchActivity
import com.usalama.usalamatechnology.shuga.utils.onClick

class SubscriptionActivity : BaseActivity() {
    private lateinit var binding: ActivitySubscriptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubscriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.finish.onClick { launchActivity<DashboardActivity> { } }
    }
}