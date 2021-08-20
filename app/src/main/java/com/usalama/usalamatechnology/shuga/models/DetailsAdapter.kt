package com.usalama.usalamatechnology.shuga.models

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.usalama.usalamatechnology.shuga.R
import com.usalama.usalamatechnology.shuga.activity.ChatActivity
import com.usalama.usalamatechnology.shuga.activity.UserDetailActivity
import kotlinx.android.synthetic.main.da_item_chat.view.*

class DetailsAdapter(private val userList : ArrayList<Users>) : RecyclerView.Adapter<DetailsAdapter.MyViewHolder>() {
    //private lateinit var mListener : onItemClickListener

    /* interface onItemClickListener{
         fun onItemClick(position:Int)
     }

     fun setOnItemClickListener(listener: onItemClickListener){
         mListener= listener
     }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.da_item_user_detail,
            parent,false)
        return MyViewHolder(itemView/*, mListener*/)

    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = userList[position]
        Picasso.get().load(currentitem.profileUrl!!).into(holder.profileUrl)
        /*holder.firstName.text = currentitem.firstName
        holder.lastName.text = currentitem.lastName
        holder.age.text = currentitem.age*/
        holder.itemView.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                val activity=v!!.context as AppCompatActivity
                val intent= Intent(activity, UserDetailActivity::class.java)
                intent.putExtra("detail", currentitem)
                Log.d("first", "$currentitem")
                activity.startActivity(intent)
            }
        })


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