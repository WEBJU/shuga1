package com.usalama.usalamatechnology.shuga.models

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.usalama.usalamatechnology.shuga.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.da_item_chat.view.*

class LatestMessageRow(val chatMessage: ChatMessage): Item<GroupieViewHolder>() {
        var chatPartnerUser: User? = null

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.tvChatMessage.text = chatMessage.text

            val chatPartnerId: String
            if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                chatPartnerId = chatMessage.toId
            } else {
                chatPartnerId = chatMessage.fromId
            }

            val ref = FirebaseDatabase.getInstance().getReference("/users/shugas/$chatPartnerId")
            ref.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    val chatPartnerUser = p0.getValue(Users::class.java)
                    if (chatPartnerUser != null) {
                        viewHolder.itemView.tvChatMessage.text = chatPartnerUser.lastName
                        val targetImageView = viewHolder.itemView.ivUser
                        Picasso.get().load(chatPartnerUser.profileUrl).into(targetImageView)
                    }
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
        }

        override fun getLayout(): Int {
            return R.layout.da_item_chat
        }
    }
