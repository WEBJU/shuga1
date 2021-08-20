package com.usalama.usalamatechnology.shuga.activity

import android.R.color
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.usalama.usalamatechnology.shuga.BaseActivity
import com.usalama.usalamatechnology.shuga.R
import com.usalama.usalamatechnology.shuga.databinding.ActivityOtpBinding
import com.usalama.usalamatechnology.shuga.utils.DAOTPEditText
import com.usalama.usalamatechnology.shuga.utils.invalidateButton
import com.usalama.usalamatechnology.shuga.utils.launchActivity
import com.usalama.usalamatechnology.shuga.utils.onClick

class OtpActivity : BaseActivity() {
    private lateinit var binding: ActivityOtpBinding

    private var mEds: Array<EditText?>? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mEds = arrayOf(binding.edDigit1, binding.edDigit2, binding.edDigit3, binding.edDigit4, binding.edDigit5, binding.edDigit6)
        binding.ivBack.onClick {
            onBackPressed()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DAOTPEditText(
                    mEds!!,
                    this,
                    getDrawable(color.transparent)!!,
                    getDrawable(R.drawable.da_bg_unselected_dot)!!
            )
        }

        mEds!!.forEachIndexed { _, editText ->
            editText?.onFocusChangeListener = focusChangeListener
            editText?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    invalidateButton(p0.toString().isNotEmpty(), binding.continued)
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

            })
        }
        binding.continued.onClick { launchActivity<SignUpAs> { } }

//        binding.continued.onClick { launchActivity<OtpActivity> { } }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    val focusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
        if (hasFocus)
            (v as EditText).background = getDrawable(color.transparent)
    }
}