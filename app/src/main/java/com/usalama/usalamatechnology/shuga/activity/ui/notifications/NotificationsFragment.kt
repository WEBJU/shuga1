package com.usalama.usalamatechnology.shuga.activity.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.usalama.usalamatechnology.shuga.R
import com.usalama.usalamatechnology.shuga.activity.ChatActivity
import com.usalama.usalamatechnology.shuga.activity.ui.dashboard.DashboardFragment
import com.usalama.usalamatechnology.shuga.databinding.FragmentNotificationsBinding
import com.usalama.usalamatechnology.shuga.models.*
import com.usalama.usalamatechnology.shuga.utils.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.da_item_chat.view.*
import kotlinx.android.synthetic.main.da_item_user.view.*
import kotlinx.android.synthetic.main.fragment_notifications.*

class NotificationsFragment : Fragment() {

    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerview : RecyclerView
    private lateinit var userArrayList : ArrayList<ChatMessage>
    var homeFragment = DashboardFragment()

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val adapter= GroupAdapter<GroupieViewHolder>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val callBack = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameContainer, homeFragment)
                    .commitNow()
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callBack)
        /*notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)*/

        //_binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = LayoutInflater.from(activity).inflate(R.layout.fragment_notifications, container, false)
        userRecyclerview = root!!.findViewById(R.id.rvAllMessages)
        userRecyclerview.layoutManager = LinearLayoutManager(context)
        userRecyclerview.setHasFixedSize(true)
        userRecyclerview.adapter = adapter


        getUser()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    var latestMessagesMap = HashMap<String, ChatMessage>()
    private fun getUser() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }
            override fun onChildRemoved(p0: DataSnapshot) {

            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })

    }

    private fun refreshRecyclerViewMessages() {
        adapter.clear()
        latestMessagesMap.values.forEach {
            adapter.add(LatestMessageRow(it))
        }
    }


}
class LatestMessageRow(val chatMessage:ChatMessage): Item<GroupieViewHolder>() {
    var chatPartnerUser: Users? = null
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
                chatPartnerUser = p0.getValue(Users::class.java)
                viewHolder.itemView.tvUserName.text = chatPartnerUser?.firstName

                val targetImageView = viewHolder.itemView.ivUser
                Picasso.get().load(chatPartnerUser?.profileUrl).into(targetImageView)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
        viewHolder.itemView.llMain.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                val activity=v!!.context as AppCompatActivity
                val intent= Intent(activity, ChatActivity::class.java)

                val ref = FirebaseDatabase.getInstance().getReference("/users/shugas/$chatPartnerId")
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

    override fun getLayout(): Int {
        return R.layout.da_item_chat
    }

}