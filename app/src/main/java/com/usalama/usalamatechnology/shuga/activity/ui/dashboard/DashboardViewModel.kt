package com.usalama.usalamatechnology.shuga.activity.ui.dashboard

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.usalama.usalamatechnology.shuga.R
import com.usalama.usalamatechnology.shuga.activity.ChatActivity
import com.usalama.usalamatechnology.shuga.activity.CongratulationsActivity
import com.usalama.usalamatechnology.shuga.activity.UserDetailActivity
import com.usalama.usalamatechnology.shuga.activity.ui.ProfileFragment
import com.usalama.usalamatechnology.shuga.models.Pic
import com.usalama.usalamatechnology.shuga.models.Pics
import com.usalama.usalamatechnology.shuga.models.Users
import com.usalama.usalamatechnology.shuga.utils.launchActivity

class MyDashboardAdapter(private val userList : ArrayList<Users>) : RecyclerView.Adapter<MyDashboardAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.da_item_user,
            parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = userList[position]
        Picasso.get().load(currentitem.profileUrl!!).into(holder.profileUrl)
        /*holder.firstName.text = currentitem.firstName
        holder.lastName.text = currentitem.lastName
        holder.age.text = currentitem.age*/

        holder.itemView.setOnClickListener { v ->
            //val myId = FirebaseAuth.getInstance().uid

            FirebaseDatabase.getInstance().getReference("users").child("shugas").child("${currentitem.uid}").child("pictures").addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()){

                            //val pics= userSnapshot.getValue(Pic::class.java)
                            val activity = v!!.context as AppCompatActivity
                            var intent = Intent(activity, UserDetailActivity::class.java)
                            intent.putExtra("detail", currentitem)
                            intent.putExtra("pic", "${snapshot.child("img1").value}")
                        Log.d("first", "${snapshot.child("img1").value}")

                            activity.startActivity(intent)
                            //imageArray.add(userSnapshot.child("img1").value)
                            //Log.d("first", "$imageArray")


                        //checkLiked()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            })
        }


    }


    override fun getItemCount(): Int {

        return userList.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        /*val firstName : TextView = itemView.findViewById(R.id.tvChatMessage)
        val lastName : TextView = itemView.findViewById(R.id.tvUserName)
        val age : TextView = itemView.findViewById(R.id.tvTime)*/
        val profileUrl : ImageView=itemView.findViewById(R.id.ivDateProfile)

    }

}

