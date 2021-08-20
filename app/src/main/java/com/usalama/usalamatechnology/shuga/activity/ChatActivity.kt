package com.usalama.usalamatechnology.shuga.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.usalama.usalamatechnology.shuga.BaseActivity
import com.usalama.usalamatechnology.shuga.R
import com.usalama.usalamatechnology.shuga.models.*
import com.usalama.usalamatechnology.shuga.utils.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.da_chat_layout_bottom.*
import kotlinx.android.synthetic.main.da_item_chat.view.*
import kotlinx.android.synthetic.main.da_item_user.view.*
import kotlinx.android.synthetic.main.item_chat_histroy.*
import kotlinx.android.synthetic.main.item_chat_histroy.view.*
import kotlinx.android.synthetic.main.item_chat_histroy.view.cardPhoto
import kotlinx.android.synthetic.main.item_chat_photo.view.*

class ChatActivity : BaseActivity() {
    companion object {
        private var MESSAGE = "Message"
        private var VOICE_MESSAGE = "Voice Message"
        private var MEDIA = "Media"

    }
    val adapter = GroupAdapter<GroupieViewHolder>()

    var toUser: Users? = null
    private lateinit var ref : DatabaseReference
    private var messageType: String = MESSAGE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        var layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.gravity = Gravity.START
        var layoutParams2 = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams2.gravity = Gravity.END
        ivBack.onClick {
            onBackPressed()
        }
        toUser = intent.getParcelableExtra<Users>("profile")
        Log.d("card","$toUser")
        tvUserName.text = toUser?.lastName


        val width = (getDisplayWidth() / 1.5).toInt()
        var photoParam = FrameLayout.LayoutParams(
            width,
            width
        )
        photoParam.gravity = Gravity.START

        var photoParam2 = FrameLayout.LayoutParams(
            width,
            width
        )
        photoParam2.gravity = Gravity.END

        /*var mChatHistoryAdapter = RecyclerViewAdapter<DaChat>(
            R.layout.item_chat_histroy,
            onBind = { view: View, badge: DaChat, i: Int ->
                if (badge.type == "Message") {
                    view.tvMessage.text = badge.chat
                } else if (badge.type == "Voice Message") {
                    view.tvVoiceMessage.text = badge.chat
                }
                view.tvMessage.hide()
                view.tvVoiceMessage.hide()
                view.cardPhoto.hide()
                if (badge.isSender) {
                    when {
                        badge.type == "Message" -> {
                            view.tvMessage.show()

                            view.tvMessage.layoutParams = layoutParams2
                            view.tvMessage.background =
                                resources.getDrawable(R.drawable.da_bg_chat_history_grd)
                            view.tvMessage.setTextColor(color(R.color.da_white))

                        }
                        badge.type == "Voice Message" -> {
                            view.tvVoiceMessage.show()
                            view.tvVoiceMessage.layoutParams = layoutParams2
                            view.tvVoiceMessage.background =
                                resources.getDrawable(R.drawable.da_bg_chat_history_grd)
                            view.tvVoiceMessage.setTextColor(color(R.color.da_white))

                        }
                        else -> {
                            view.cardPhoto.show()
                            view.cardPhoto.layoutParams = photoParam2
                            Log.d("card","${badge.img}")
                            Picasso.get().load(badge.img!!).into(view.ivChatPhoto)
                            //view. ivChatPhoto.setImageResource(badge.img!!)
                        }
                    }
                    view.ivChatProfile.visibility = View.INVISIBLE
                } else {
                    when {
                        badge.type == "Message" -> {
                            view.tvMessage.show()
                            view.tvMessage.layoutParams = layoutParams
                            view.tvMessage.background =
                                resources.getDrawable(R.drawable.da_bg_chat_history)
                            view.tvMessage.setTextColor(color(R.color.da_textColorPrimary))

                        }
                        badge.type == "Voice Message" -> {
                            view.tvVoiceMessage.show()
                            view.tvVoiceMessage.layoutParams = layoutParams
                            view.tvVoiceMessage.background =
                                resources.getDrawable(R.drawable.da_bg_chat_history)
                            view.tvVoiceMessage.setTextColor(color(R.color.da_textColorPrimary))
                        }
                        else -> {
                            view.cardPhoto.show()
                            view.cardPhoto.layoutParams = photoParam
                            Picasso.get().load(badge.img!!).into(view.ivChatPhoto)
                           // view.ivChatPhoto.setImageResource(badge.img!!)
                        }
                    }
                    if (badge.showProfile) {
                        view.ivChatProfile.show()
                    } else {
                        view.ivChatProfile.invisible()
                    }

                }

            })
        rvChat.setAdapter(adapter)
        //rvChat.setAdapter(adapter)
        rvChat.apply {
            setVerticalLayout()
            rvChat.setAdapter(adapter)
            //adapter = mChatHistoryAdapter

        }*/
        rvChat.adapter = adapter

        var photoAdapter = RecyclerViewAdapter<DaPhoto>(
            R.layout.item_chat_photo,
            onBind = { view: View, daPhoto: DaPhoto, i: Int ->
                Picasso.get().load(daPhoto.img!!).into(view.ivPhoto)
                //view.ivPhoto.setImageResource(daPhoto.img!!)
            })
        var gifAdapter = RecyclerViewAdapter<DaPhoto>(
            R.layout.item_chat_photo,
            onBind = { view: View, daPhoto: DaPhoto, i: Int ->
                Picasso.get().load(daPhoto.img!!).into(view.ivPhoto)
            })
        rvPhoto.apply {
            layoutManager = GridLayoutManager(this@ChatActivity, 3)
            setHasFixedSize(true)
            adapter = photoAdapter
        }
        rvGif.apply {
            layoutManager = GridLayoutManager(this@ChatActivity, 3)
            setHasFixedSize(true)
            adapter = gifAdapter
        }
        //gifAdapter.addItems(getGalleryPhotos())

        //photoAdapter.addItems(getGalleryPhotos())
        /*photoAdapter.onItemClick = { i: Int, view: View, daPhoto: DaPhoto ->
            var badge2 = DaChat()
            badge2.img = daPhoto.img
            badge2.type = MEDIA
            badge2.isSender = true
            mChatHistoryAdapter.addItem(badge2)
            rvChat.scrollToPosition(mChatHistoryAdapter.itemCount - 1)

            resetAddLayout()
        }*/
        getUsersChats()

        //mChatHistoryAdapter.addItems(getUserChats())
        ivGallary.onClick {
            setSelectedImg(this)
            applyColorFilter(color(R.color.da_red))
            ivVoice.applyColorFilter(color(R.color.da_textColorSecondary))
            ivGif.applyColorFilter(color(R.color.da_textColorSecondary))
            rvPhoto.show()
            llVoice.hide()
            rvGif.hide()
            hideSoftKeyboard()
            messageType = MEDIA

        }
//        ivCall.onClick {
//            launchActivity<CallingActivity> {
//                putExtra("isCalling", true)
//            }
//        }
//        ivAppointment.onClick {
//            launchActivity<MakeAppointmentActivity>()
//        }
        ivVoice.onClick {
            rvPhoto.hide()
            rvGif.hide()
            llVoice.show()
            applyColorFilter(color(R.color.da_red))
            ivGallary.applyColorFilter(color(R.color.da_textColorSecondary))
            ivGif.applyColorFilter(color(R.color.da_textColorSecondary))
            hideSoftKeyboard()
            messageType = VOICE_MESSAGE
        }
        ivGif.onClick {
            applyColorFilter(color(R.color.da_red))
            ivGallary.applyColorFilter(color(R.color.da_textColorSecondary))
            ivVoice.applyColorFilter(color(R.color.da_textColorSecondary))
            rvPhoto.hide()
            llVoice.hide()
            rvGif.show()
            hideSoftKeyboard()
            messageType = MEDIA

        }
//        ivCamera.onClick {
//            launchActivity<DACameraActivity>()
//        }
        ivAdd.onClick {
            if (!rlAdd.isVisible) {
                setImageResource(R.drawable.da_ic_plus_gradient)
                rlAdd.show()
            } else {
                setImageResource(R.drawable.da_ic_add)
                resetAddLayout()
            }

            //  hideSoftKeyboard()

        }
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

                if (p0.toString().isNotEmpty()) {
                    messageType = MESSAGE
                    resetAddLayout()
                    ivSend.setImageResource(R.drawable.da_ic_send_gradient)
                } else {
                    messageType = ""
                    ivSend.setImageResource(R.drawable.da_ic_send_button)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
        edtSearch.setOnFocusChangeListener { _, b ->
            if (b) {
                resetAddLayout()
            }
        }
        ivSend.onClick {
            val text = edtSearch.text.toString()

            val fromId = FirebaseAuth.getInstance().uid
            val user = intent.getParcelableExtra<Users>("profile")
            val toId = user?.uid

            if (fromId == null) return@onClick

//    val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
            val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

            val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

            val chatMessage = ChatMessage(reference.key!!, text, fromId!!, toId!!, System.currentTimeMillis() / 1000)

            reference.setValue(chatMessage)
                .addOnSuccessListener {
                    Log.d("chat", "Saved our chat message: ${reference.key}")
                    edtSearch.text.clear()
                    rvChat.scrollToPosition(adapter.itemCount - 1)
                }

            toReference.setValue(chatMessage)

            val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
            latestMessageRef.setValue(chatMessage)

            val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
            latestMessageToRef.setValue(chatMessage)
        }
            /*var badge2 = DaChat()
            if (messageType == MESSAGE) {
                badge2.chat = edtSearch.text.toString()

            } else if (messageType == VOICE_MESSAGE) {
                badge2.chat = "00:12"
            }
            badge2.type = messageType
            badge2.isSender = true
            mChatHistoryAdapter.addItem(badge2)
            edtSearch.text.clear()
            rvChat.scrollToPosition(mChatHistoryAdapter.itemCount - 1)*/
        }



    private fun getUsersChats() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        ref.addChildEventListener(object: ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)

                if (chatMessage != null) {

                    if (chatMessage.fromId == fromId) {
                        val currentUser = DashboardActivity.currentUser ?: return
                        adapter.add(ChatFromItem(chatMessage.text, currentUser))
                    } else {
                        adapter.add(ChatToItem(chatMessage.text, toUser!!))
                    }
                }

                rvChat.scrollToPosition(adapter.itemCount - 1)

            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })    }

    private fun setSelectedImg(imageView: ImageView?) {
        ivGallary.applyColorFilter(color(R.color.da_textColorSecondary))
        ivVoice.applyColorFilter(color(R.color.da_textColorSecondary))
        ivGif.applyColorFilter(color(R.color.da_textColorSecondary))
        imageView?.apply {
            applyColorFilter(color(R.color.da_red))
        }
    }

    private fun resetAddLayout() {
        rlAdd.hide()
        rvPhoto.hide()
        llVoice.hide()
        rvGif.hide()
        ivAdd.setImageResource(R.drawable.da_ic_add)
    }

}

