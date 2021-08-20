package com.usalama.usalamatechnology.shuga.activity.ui

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.usalama.usalamatechnology.shuga.R
import com.usalama.usalamatechnology.shuga.activity.*
import com.usalama.usalamatechnology.shuga.activity.ui.dashboard.DashboardFragment
import com.usalama.usalamatechnology.shuga.activity.ui.notifications.NotificationsFragment
import com.usalama.usalamatechnology.shuga.models.Users
import com.usalama.usalamatechnology.shuga.utils.launchActivity
import com.usalama.usalamatechnology.shuga.utils.onClick
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.lang.NullPointerException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var dbref : DatabaseReference

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var messageFragment = NotificationsFragment()
    var homeFragment = DashboardFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkUser()
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    private fun checkUser() {
            //val firebaseUser= firebaseAuth.currentUser
        val uid = FirebaseAuth.getInstance().uid
        dbref = FirebaseDatabase.getInstance().getReference("users")

        dbref.child("shugas").child(uid!!).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    val user = snapshot.getValue(Users::class.java)

                    if (user != null) {
                        try {
                            tvUsername.text = user.firstName
                            Picasso.get().load(user.profileUrl).into(ivProfile)
                        } catch(e: NullPointerException) {
                            tvUsername.text = "Name"
                            Picasso.get().load(user.profileUrl).into(ivProfile)
                        }


                    } else {
                        tvUsername.text = "name"
                        Picasso.get().load(user?.profileUrl).into(ivProfile)
                    }

                    /*for (userSnapshot in snapshot.children){


                        val user = userSnapshot.getValue(Users::class.java)
                        Log.d("TAG", "$user")

                    }*/


                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

        //Picasso.get().load(currentitem.profileUrl!!).into(holder.profileUrl)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvSecurity.onClick {
            requireActivity().launchActivity<SecurityActivity>()
        }

        tvFilter.onClick {
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameContainer, messageFragment)
                .commitNow()
        }

        connectInstaNotification.onClick {
            val uriForApp: Uri = Uri.parse("http://instagram.com/_u/")
            val forApp = Intent(Intent.ACTION_VIEW, uriForApp)

            val uriForBrowser: Uri = Uri.parse("http://instagram.com/")
            val forBrowser = Intent(Intent.ACTION_VIEW, uriForBrowser)

            forApp.component =
                ComponentName(
                    "com.instagram.android",
                    "com.instagram.android.activity.UrlHandlerActivity"
                )

            try {
                startActivity(forApp)
            } catch (e: ActivityNotFoundException) {
                startActivity(forBrowser)
            }
        }
        ivProfile.onClick {
            requireActivity().launchActivity<ViewProfileActivity>()

        }
        tvLogout.onClick {
            Firebase.auth.signOut()
            val intent= Intent(this.context, Welcome::class.java)
            intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }
    }
}