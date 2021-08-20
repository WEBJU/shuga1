package com.usalama.usalamatechnology.shuga.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.usalama.usalamatechnology.shuga.BaseActivity
import com.usalama.usalamatechnology.shuga.R
import com.usalama.usalamatechnology.shuga.databinding.ActivitySignUpDetailsBinding
import com.usalama.usalamatechnology.shuga.utils.onClick
import kotlinx.android.synthetic.main.activity_sign_up_details.*


class SignUpDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivitySignUpDetailsBinding
    lateinit var sugar:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_details)
        binding = ActivitySignUpDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        verifyUserIsLoggedIn()


        val adapter = ArrayAdapter.createFromResource(this,
            R.array.gender, android.R.layout.simple_spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        gender.adapter = adapter

        val adapter3 = ArrayAdapter.createFromResource(this,
            R.array.dob, android.R.layout.simple_spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        dob.adapter = adapter3

        val adapter2 = ArrayAdapter.createFromResource(this,
            R.array.gender2, android.R.layout.simple_spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        gender2.adapter = adapter2


        binding.first.onClick{
            binding.first.setBackgroundColor(ContextCompat.getColor(context, R.color.shuga_red))
            binding.second.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            binding.third.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            //saveProfileToFirebase("20k - 100k")
            sugar = "20k - 100k"
        }
        binding.second.onClick{
            binding.first.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            binding.second.setBackgroundColor(ContextCompat.getColor(context, R.color.shuga_red))
            binding.third.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            //saveProfileToFirebase("100k - 250k")
            sugar = "100k - 250k"
        }
        binding.third.onClick{
            binding.first.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            binding.second.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            binding.third.setBackgroundColor(ContextCompat.getColor(context, R.color.shuga_red))
            //saveProfileToFirebase("250k - 750k")
            sugar = "250k - 750k"
        }

        //signupuser()
        binding.continueBtn.setOnClickListener {
            saveProfileToFirebase(sugar)
        }

    }

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid

        if(uid==null){
            val intent=Intent(this, SignUpActivity::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
    private fun saveProfileToFirebase(sugar: String) {
        val firstName=binding.firstName.text.toString()
        val lastName=binding.lastName.text.toString()
        val gender=gender.selectedItem.toString()
        val age=binding.year.text.toString()
        val dob=dob.selectedItem.toString()
        val gender2=gender2.selectedItem.toString()
        val domain = intent.getStringExtra("detail")
        Log.d("first", "$domain")
        if (firstName.isNotEmpty()&&lastName.isNotEmpty()&&age.isNotEmpty()&&dob.isNotEmpty()&&sugar.isNotEmpty()){
            val intent=Intent(this, SignUpProfilePicActivity::class.java)
            intent.putExtra("gender", "$gender")
            intent.putExtra("firstName", "$firstName")
            intent.putExtra("lastName", "$lastName")
            intent.putExtra("age", "$age")
            intent.putExtra("dob", "$dob")
            intent.putExtra("gender2", "$gender2")
            intent.putExtra("domain", "$domain")
            intent.putExtra("sugar", "$sugar")
            startActivity(intent)


        }else{
            Toast.makeText(this, "Empty Fields",Toast.LENGTH_SHORT).show()
        }
    }

    /* binding.registerBtn.setOnClickListener {

         val firstName = binding.firstName.text.toString()
         val lastName = binding.lastName.text.toString()
         val age = binding.age.text.toString()
         val userName = binding.userName.text.toString()

         database = FirebaseDatabase.getInstance().getReference("Users")
         val User = User(firstName,lastName,age,userName)
         database.child(userName).setValue(User).addOnSuccessListener {

             binding.firstName.text.clear()
             binding.lastName.text.clear()
             binding.age.text.clear()
             binding.userName.text.clear()

             Toast.makeText(this,"Successfully Saved",Toast.LENGTH_SHORT).show()

         }.addOnFailureListener{

             Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()


         }*/


}

