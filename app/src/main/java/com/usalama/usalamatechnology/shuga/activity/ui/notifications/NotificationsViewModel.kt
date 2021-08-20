package com.usalama.usalamatechnology.shuga.activity.ui.notifications

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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
import com.usalama.usalamatechnology.shuga.activity.SignUpDetailsActivity
import com.usalama.usalamatechnology.shuga.activity.ui.dashboard.DashboardFragment
import com.usalama.usalamatechnology.shuga.models.ChatMessage
import com.usalama.usalamatechnology.shuga.models.Users
import com.usalama.usalamatechnology.shuga.utils.launchActivity
import com.usalama.usalamatechnology.shuga.utils.onClick
import kotlinx.android.synthetic.main.da_item_chat.view.*

class MyNotificationsAdapter(private val userList : ArrayList<ChatMessage>) : RecyclerView.Adapter<MyNotificationsAdapter.MyViewHolder>() {
    //private lateinit var mListener : onItemClickListener

   /* interface onItemClickListener{
        fun onItemClick(position:Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener= listener
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.da_item_chat,
            parent,false)
        return MyViewHolder(itemView/*, mListener*/)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = userList[position]

        //Picasso.get().load(currentitem.profileUrl!!).into(holder.profileUrl)
        holder.firstName.text = currentitem.text
        //holder.lastName.text = currentitem.fromId
        //holder.age.text = currentitem.timestamp.toString()
        //viewHolder.itemView.tvChatMessage.text = chatMessage.text

        val chatPartnerId: String
        if (currentitem.fromId == FirebaseAuth.getInstance().uid) {
            chatPartnerId = currentitem.toId
        } else {
            chatPartnerId = currentitem.fromId
        }

        val ref = FirebaseDatabase.getInstance().getReference("/users/sponsors/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val chatPartnerUser = p0.getValue(Users::class.java)
                if (chatPartnerUser != null) {
                    holder.lastName.text = chatPartnerUser.lastName
                        //val targetImageView = viewHolder.itemView.ivUser
                    Picasso.get().load(chatPartnerUser.profileUrl).into(holder.profileUrl)
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
        holder.itemView.llMain.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                val activity=v!!.context as AppCompatActivity
                val intent= Intent(activity, ChatActivity::class.java)
                val chatee: String = if (currentitem.fromId == FirebaseAuth.getInstance().uid) {
                    currentitem.toId
                } else {
                    currentitem.fromId
                }

                val ref = FirebaseDatabase.getInstance().getReference("/users/sponsors/$chatee")
                ref.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        val chatPartnerUser = p0.getValue(Users::class.java)
                        if (chatPartnerUser != null) {
                            intent.putExtra("profile", chatPartnerUser)
                            activity.startActivity(intent)
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                })



                /*val chatActivity= ChatActivity()
                activity.supportFragmentManager.beginTransaction().replace(R.id......, chatActivity).addToBackStack(null).commit()

                activity.launchActivity<ChatActivity>()
                Toast.makeText(activity, "item clicked: ${currentitem.firstName}",
                    Toast.LENGTH_SHORT).show()*/
            }
        })


    }

    override fun getItemCount(): Int {
        return userList.size
    }


    class MyViewHolder(itemView : View/*, listener: onItemClickListener*/) : RecyclerView.ViewHolder(itemView){

        val firstName : TextView = itemView.findViewById(R.id.tvChatMessage)
        val lastName : TextView = itemView.findViewById(R.id.tvUserName)
        val age : TextView = itemView.findViewById(R.id.tvTime)
        val profileUrl : ImageView=itemView.findViewById(R.id.ivUser)

    }

}