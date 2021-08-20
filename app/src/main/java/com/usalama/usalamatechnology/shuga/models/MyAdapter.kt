package com.usalama.usalamatechnology.shuga.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.usalama.usalamatechnology.shuga.R

class MyAdapter(private val userList : ArrayList<Users>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.da_item_chat,
        parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = userList[position]

        holder.firstName.text = currentitem.firstName
        holder.lastName.text = currentitem.lastName
        holder.age.text = currentitem.age

    }

    override fun getItemCount(): Int {

        return userList.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val firstName : TextView = itemView.findViewById(R.id.tvChatMessage)
        val lastName : TextView = itemView.findViewById(R.id.tvUserName)
        val age : TextView = itemView.findViewById(R.id.tvTime)

    }

}