package com.usalama.usalamatechnology.shuga.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.usalama.usalamatechnology.shuga.BaseActivity
import com.usalama.usalamatechnology.shuga.R
import com.usalama.usalamatechnology.shuga.databinding.ActivitySignUpAsBinding
import com.usalama.usalamatechnology.shuga.utils.launchActivity
import com.usalama.usalamatechnology.shuga.utils.onClick
import kotlinx.android.synthetic.main.activity_sign_up_as.*

class SignUpAs : BaseActivity() {
    private lateinit var binding: ActivitySignUpAsBinding
    private lateinit var domain:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpAsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ivBack.onClick {
            onBackPressed()
        }
        //checkUser()

        binding.sponsor.onClick {
            binding.sponsor.setBackgroundColor(ContextCompat.getColor(context, R.color.shuga_red))
            binding.sugarBaby.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            domain= "sponsor"

//            binding.sugarBaby.setBackgroundColor(R.color.white)
//            binding.sugarBaby.setTextColor(R.color.da_textColorPrimary)
        }
        binding.sugarBaby.onClick {
            binding.sugarBaby.setBackgroundColor(ContextCompat.getColor(context, R.color.shuga_red))
            binding.sponsor.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            domain= "sugarbaby"

        //            binding.sponsor.setBackgroundColor(R.color.white)
//            binding.sponsor.setTextColor(R.color.da_textColorPrimary)
        }

        continueBtn.onClick {
            val intent= Intent(this@SignUpAs, SignUpDetailsActivity::class.java)
            Log.d("first", "$domain")
            intent.putExtra("detail", "$domain")
            Log.d("first", "$domain")
            startActivity(intent)}
        /*launchActivity<SignUpProfilePicActivity> { }*//* }*/
    }
}