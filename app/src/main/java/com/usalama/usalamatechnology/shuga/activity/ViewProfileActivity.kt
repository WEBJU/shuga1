package com.usalama.usalamatechnology.shuga.activity

import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.usalama.usalamatechnology.shuga.BaseActivity
import com.usalama.usalamatechnology.shuga.databinding.ActivityViewProfileBinding
import com.usalama.usalamatechnology.shuga.models.User
import com.usalama.usalamatechnology.shuga.models.Users
import com.usalama.usalamatechnology.shuga.utils.launchActivity
import com.usalama.usalamatechnology.shuga.utils.makeNormalStatusBar
import com.usalama.usalamatechnology.shuga.utils.makeTransaprant
import com.usalama.usalamatechnology.shuga.utils.onClick

class ViewProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityViewProfileBinding
    private lateinit var dbref : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkUser()
        binding = ActivityViewProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        makeNormalStatusBar()
        makeTransaprant()
        binding.ivEdit.onClick {
            launchActivity<ProfileActivity>()
        }
        //val user = intent.getParcelableExtra<Users>("detail")
        val uid = FirebaseAuth.getInstance().uid
        dbref = FirebaseDatabase.getInstance().getReference("users")

        dbref.child("shugas").child("$uid").child("about").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                if (snapshot.exists()){
                    val about = snapshot.getValue(User::class.java)
                    if (about != null) {
                        binding.edtMobileNum.text = "Phone: "+about.phone
                        binding.edtLocation.text = "Location: "+about.location
                        binding.tvMore.text = "Profession: "+about.work
                        binding.tvMoreAbout.text = "Status: "+about.status
                    }

                }else{
                    binding.edtMobileNum.text = "Phone: "+"N/A"
                    binding.edtLocation.text = "Location: "+"N/A"
                    binding.tvMore.text = "Profession: "+"N/A"
                    binding.tvMoreAbout.text = "Status: "+"N/A"
                }
            } catch (e: NullPointerException){
                    binding.edtMobileNum.text = "Phone: "+"N/A"
                    binding.edtLocation.text = "Location: "+"N/A"
                    binding.tvMore.text = "Profession: "+"N/A"
                    binding.tvMoreAbout.text = "Status: "+"N/A"

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
        binding.ivBack.onClick {
            onBackPressed()
        }
    }

    private fun checkUser() {
        val uid = FirebaseAuth.getInstance().uid
        dbref = FirebaseDatabase.getInstance().getReference("users")

        dbref.child("shugas").child(uid!!).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    val user = snapshot.getValue(Users::class.java)
                    Log.d("TAG", "${user!!.firstName}")
                    Picasso.get().load(user!!.profileUrl!!).into(binding.ivPhoto)
                    binding.tvName.text = user!!.firstName
                    binding.tvProfession.text = user!!.gender
                    binding.tvAbout.text = "Age: "+ user!!.age+"\n Interested in: " + user!!.gender2
                    /*for (userSnapshot in snapshot.children){


                        val user = userSnapshot.getValue(Users::class.java)
                        Log.d("TAG", "$user")

                    }*/


                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }
}