package com.usalama.usalamatechnology.shuga.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.usalama.usalamatechnology.shuga.R
import com.usalama.usalamatechnology.shuga.databinding.ActivityCongratulationsBinding
import com.usalama.usalamatechnology.shuga.databinding.ActivityDashboardBinding
import com.usalama.usalamatechnology.shuga.databinding.ActivitySignUpProfilePicBinding
import com.usalama.usalamatechnology.shuga.models.Users
import kotlinx.android.synthetic.main.activity_congratulations.*
import kotlinx.android.synthetic.main.item_chat_photo.view.*

class CongratulationsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCongratulationsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCongratulationsBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_congratulations)
        setContentView(binding.root)

        //binding = ActivityCongratulationsBinding.inflate(layoutInflater)
        val match = intent.getParcelableExtra<Users>("profileUrl")
        Log.d("first", "$match")
        if (match != null) {
            Picasso.get().load(match.profileUrl!!).into(binding.ivProfile2)
        }

        var user= FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/shugas/$user")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                val currentUser = p0.getValue(Users::class.java)
                Picasso.get().load(currentUser?.profileUrl!!).into(binding.ivProfile1)
                //Log.d("LatestMessages", "Current user ${DashboardActivity.currentUser?.profileUrl}")
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        binding.btnSendMessage.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val activity=v!!.context as AppCompatActivity
                val intent= Intent(activity, ChatActivity::class.java)
                intent.putExtra("profile", match)
                intent.putExtra("uid", "${match?.uid}")
                intent.putExtra("name", "${match?.firstName}")
                activity.startActivity(intent)
                /*val chatActivity= ChatActivity()
                activity.supportFragmentManager.beginTransaction().replace(R.id......, chatActivity).addToBackStack(null).commit()

                activity.launchActivity<ChatActivity>()
                Toast.makeText(activity, "item clicked: ${currentitem.firstName}",
                    Toast.LENGTH_SHORT).show()*/
            }})


    }
}