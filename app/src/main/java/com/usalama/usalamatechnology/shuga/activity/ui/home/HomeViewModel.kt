package com.usalama.usalamatechnology.shuga.activity.ui.home

import android.content.Intent
import android.content.res.Resources
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.usalama.usalamatechnology.shuga.R
import com.usalama.usalamatechnology.shuga.activity.ChatActivity
import com.usalama.usalamatechnology.shuga.models.Users
import com.usalama.usalamatechnology.shuga.utils.launchActivity
import kotlinx.android.synthetic.main.da_item_chat.view.*


class MyHomeAdapter(private val userList : ArrayList<Users>) : RecyclerView.Adapter<MyHomeAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_match,
            parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val width = (Resources.getSystem().displayMetrics.widthPixels / 4.2).toInt()
        var layoutParams = CoordinatorLayout.LayoutParams(width, width)
        layoutParams.bottomMargin = width / 8
        var layoutParams2 =
            CoordinatorLayout.LayoutParams((width / 2.5).toInt(), (width / 2.5).toInt())
        layoutParams2.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        val currentitem = userList[position]

        holder.profileUrl.layoutParams = layoutParams
        holder.chatico.layoutParams = layoutParams2
        holder.chatico.setPadding(width / 10, width / 10, width / 10, width / 10)
        /*if (i % 3 == 1) {
            holder.viewDummy.visibility = View.VISIBLE
        } else {
            holder.viewDummy.visibility = View.GONE
        }*/
        Picasso.get().load(currentitem.profileUrl!!).into(holder.profileUrl)
        //holder.profileUrl.setImageResource(currentitem.profileUrl!!)
        //holder.tvName.text = user.firstName
        holder.firstName.text = currentitem.firstName
        //holder.profileUrl.text = currentitem.profileUrl
        holder.itemView.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                val activity=v!!.context as AppCompatActivity
                val intent= Intent(activity, ChatActivity::class.java)
                intent.putExtra("profile", currentitem)
                intent.putExtra("uid", "${currentitem.uid}")
                intent.putExtra("name", "${currentitem.firstName}")
                activity.startActivity(intent)
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


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val firstName : TextView = itemView.findViewById(R.id.tvName)
        val profileUrl : ImageView = itemView.findViewById(R.id.ivProfile)
        val chatico : ImageView = itemView.findViewById(R.id.ivImg)
    }

}