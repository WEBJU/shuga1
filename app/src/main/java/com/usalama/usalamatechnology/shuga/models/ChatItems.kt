package com.usalama.usalamatechnology.shuga.models


import android.util.Log
import com.squareup.picasso.Picasso
import com.usalama.usalamatechnology.shuga.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_from_item.view.*
import kotlinx.android.synthetic.main.chat_to_item.view.*
import kotlinx.android.synthetic.main.item_chat_histroy.view.*


class ChatFromItem(var text: String, var user: Users): Item<GroupieViewHolder>() {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.tvMessage.text = text
            Log.d("chat", "my text is:$text")
            //val uri = user.profileUrl
            //val targetImageView = viewHolder.itemView.imageview_chat_from_row
            //Picasso.get().load(uri).into(targetImageView)
        }

        override fun getLayout(): Int {
            return R.layout.item_chat_histroy
        }

}

class ChatToItem(var text: String, var user: Users): Item<GroupieViewHolder>() {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.textview_from_row.text = text
            Log.d("chat", "my tes:$text")
            // load our user image into the star
            val uri = user.profileUrl
            val targetImageView = viewHolder.itemView.imageview_chat_from_row
            Picasso.get().load(uri).into(targetImageView)
        }

        override fun getLayout(): Int {
            return R.layout.chat_to_item
        }


    }
