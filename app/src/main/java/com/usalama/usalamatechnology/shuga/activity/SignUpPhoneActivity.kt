package com.usalama.usalamatechnology.shuga.activity


import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.hbb20.CountryCodePicker
import com.hbb20.CountryCodePicker.OnCountryChangeListener
import com.usalama.usalamatechnology.shuga.BaseActivity
import com.usalama.usalamatechnology.shuga.R
import com.usalama.usalamatechnology.shuga.databinding.ActivitySignUpPhoneBinding
import com.usalama.usalamatechnology.shuga.utils.DAOTPEditText
import com.usalama.usalamatechnology.shuga.utils.invalidateButton
import com.usalama.usalamatechnology.shuga.utils.onClick
import java.util.concurrent.TimeUnit

class SignUpPhoneActivity : BaseActivity() {

    private lateinit var binding: ActivitySignUpPhoneBinding

    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken?=null

    private var mCallBacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks? =null

    private var mVerificationId:String?=null

    private lateinit var firebaseAuth: FirebaseAuth

    private val TAG = "MAIN_TAG"
    private var mEds: Array<EditText?>? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("UseCompatLoadingForDrawables")

    private lateinit var progressDialog: ProgressDialog
    private var ccp: CountryCodePicker?=null
    private var countryCode:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth= FirebaseAuth.getInstance()
        checkUser()

        binding.phoneLl.visibility= View.VISIBLE
        binding.codeLl.visibility = View.GONE


        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        mCallBacks= object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                progressDialog.dismiss()
                Toast.makeText(this@SignUpPhoneActivity, "${e.message}", Toast.LENGTH_SHORT).show()

            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                Log.d(TAG, "onCodeSent: $verificationId")
                mVerificationId=verificationId
                forceResendingToken= token
                progressDialog.dismiss()

                binding.phoneLl.visibility= View.GONE
                binding.codeLl.visibility = View.VISIBLE

                Toast.makeText(this@SignUpPhoneActivity, "Verification Code Sent...", Toast.LENGTH_SHORT).show()
                binding.codeSentDescriptionTv.text="${binding.phoneEt.text.toString().trim()}"
            }
        }
        ccp = findViewById(R.id.countryCodePicker)
        
        binding.continueBtn.setOnClickListener{
            var phone= binding.phoneEt.text.toString().trim()

            if (TextUtils.isEmpty(phone)){
                Toast.makeText(this@SignUpPhoneActivity, "Enter Phone Number...", Toast.LENGTH_SHORT).show()
            }else if(phone.length==10)
            {
                countryCode=ccp!!.selectedCountryCodeWithPlus
                phone="+"+countryCode + phone
                startPhoneNumberVerification(phone) }else{
                startPhoneNumberVerification(phone)
                }
        }
        mEds = arrayOf(binding.edDigit1, binding.edDigit2, binding.edDigit3, binding.edDigit4, binding.edDigit5, binding.edDigit6)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DAOTPEditText(
                mEds!!,
                this,
                getDrawable(android.R.color.transparent)!!,
                getDrawable(R.drawable.da_bg_unselected_dot)!!
            )
        }

        mEds!!.forEachIndexed { _, editText ->
            editText?.onFocusChangeListener = focusChangeListener
            editText?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    invalidateButton(p0.toString().isNotEmpty(), binding.loginBtn)
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

            })
        }
        binding.codeResendTv.setOnClickListener{
            val phone= binding.phoneEt.text.toString().trim()
            if (TextUtils.isEmpty(phone)){
                Toast.makeText(this@SignUpPhoneActivity, "Enter Phone Number...", Toast.LENGTH_SHORT).show()
            }else
            {resendVerificationCode(phone, forceResendingToken) }

        }
        binding.loginBtn.setOnClickListener{

            val code=binding.edDigit1.text.toString().trim()+binding.edDigit2.text.toString().trim()+binding.edDigit3.text.toString().trim()+binding.edDigit4.text.toString().trim()+binding.edDigit5.text.toString().trim()+binding.edDigit6.text.toString().trim()
            if (TextUtils.isEmpty(code)){
                Toast.makeText(this@SignUpPhoneActivity, "Enter verification code...", Toast.LENGTH_SHORT).show()
            }else
            {verificationPhoneNumberWithCode(mVerificationId!!,code) }
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

    private fun startPhoneNumberVerification(phone: String){
        progressDialog.setMessage("Verifying phone Number...")
        progressDialog.show()

        val options = PhoneAuthOptions.newBuilder(firebaseAuth).setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallBacks!!)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun resendVerificationCode(phone: String, token : PhoneAuthProvider.ForceResendingToken?) {
        progressDialog.setMessage("Resending Code...")
        progressDialog.show()

        val options = PhoneAuthOptions.newBuilder(firebaseAuth).setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallBacks!!)
            .setForceResendingToken(token!!)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    private fun verificationPhoneNumberWithCode(verificationId: String, code:String){
        progressDialog.setMessage("Verifying Code...")
        progressDialog.show()

        val credential= PhoneAuthProvider.getCredential(verificationId,code)
        signInWithPhoneAuthCredential(credential)
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential){
        progressDialog.setMessage("Logging In")

        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                progressDialog.dismiss()
                val phone= firebaseAuth.currentUser!!.phoneNumber
                Toast.makeText(this,"Logged In as $phone", Toast.LENGTH_SHORT).show()
                val intent= Intent(this, SignUpAs::class.java)
                intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener{
                    e->
                progressDialog.dismiss()
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    val focusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
        if (hasFocus)
            (v as EditText).background = getDrawable(android.R.color.transparent)
    }
}