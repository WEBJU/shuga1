package com.usalama.usalamatechnology.shuga.activity.ui.home

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.usalama.usalamatechnology.shuga.R
import com.usalama.usalamatechnology.shuga.activity.ChatActivity
import com.usalama.usalamatechnology.shuga.activity.ui.dashboard.DashboardFragment
import com.usalama.usalamatechnology.shuga.activity.ui.notifications.MyNotificationsAdapter
import com.usalama.usalamatechnology.shuga.databinding.FragmentHomeBinding
import com.usalama.usalamatechnology.shuga.models.User
import com.usalama.usalamatechnology.shuga.models.Users


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    var homeFragment = DashboardFragment()
    // This property is only valid between onCreateView and
    // onDestroyView.
    //private val binding get() = _binding!!
    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerview : RecyclerView
    private lateinit var userArrayList : ArrayList<Users>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val root: View = LayoutInflater.from(activity).inflate(R.layout.fragment_home, container, false)
        userRecyclerview = root!!.findViewById(R.id.rvMatches)
        userRecyclerview.layoutManager = GridLayoutManager(context,3)
        userRecyclerview.setHasFixedSize(true)
        userArrayList = arrayListOf()
        getUser()



        return root
    }

    private fun getUser() {
        val myid = FirebaseAuth.getInstance().uid
        dbref = FirebaseDatabase.getInstance().getReference("users")

        dbref.child("shugas").child("$myid").child("likee").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (userSnapshot in snapshot.children){


                        val user = userSnapshot.getValue(Users::class.java)
                        if (user != null) {
                            if (user.uid != FirebaseAuth.getInstance().uid){ userArrayList.add(user!!) }
                        }
                        Log.d("users", "$userArrayList")

                    }

                    userRecyclerview.adapter = MyHomeAdapter(userArrayList)
                    Log.d("users", "${userRecyclerview.adapter}")


                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val width = (requireActivity().getDisplayWidth() / 4.2).toInt()
        var layoutParams = CoordinatorLayout.LayoutParams(width, width)
        layoutParams.bottomMargin = width / 8
        var layoutParams2 =
            CoordinatorLayout.LayoutParams((width / 2.5).toInt(), (width / 2.5).toInt())
        layoutParams2.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL

        var matchAdapter = RecyclerViewAdapter<Users>(
            R.layout.item_match,
            onBind = { view: View, user: Users, i: Int ->
                view.ivProfile.layoutParams = layoutParams
                view.ivImg.layoutParams = layoutParams2
                view.ivImg.setPadding(width / 10, width / 10, width / 10, width / 10)
                if (i % 3 == 1) {
                    view.viewDummy.visibility = View.VISIBLE
                } else {
                    view.viewDummy.visibility = View.GONE
                }
                Log.d("parcel", "254$user")
                Picasso.get().load(user.profileUrl!!).into(view.ivProfile)
                //view.ivProfile.setImageResource(user.img!!)
                view.tvName.text = user.firstName
            })
        rvMatches.apply {
            layoutManager = GridLayoutManager(requireActivity(), 3)
            adapter = matchAdapter
        }
        matchAdapter.addItems(generateUser())
        matchAdapter.onItemClick = { i: Int, view: View, user: Users ->
            requireActivity().launchActivity<ChatActivity>()
        }

    }*/

}