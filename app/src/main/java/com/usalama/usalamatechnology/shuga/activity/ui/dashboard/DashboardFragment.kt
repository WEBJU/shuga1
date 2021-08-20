package com.usalama.usalamatechnology.shuga.activity.ui.dashboard

import android.app.ProgressDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.usalama.usalamatechnology.shuga.R
import com.usalama.usalamatechnology.shuga.activity.CongratulationsActivity
import com.usalama.usalamatechnology.shuga.activity.ui.home.MyHomeAdapter
import com.usalama.usalamatechnology.shuga.databinding.FragmentDashboardBinding
import com.usalama.usalamatechnology.shuga.models.User
import com.usalama.usalamatechnology.shuga.models.Users
import com.usalama.usalamatechnology.shuga.utils.*
import com.usalama.usalamatechnology.shuga.utils.cardstackview.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.lang.NullPointerException

class  DashboardFragment : Fragment() {
    private lateinit var dbref : DatabaseReference
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var userArrayList : ArrayList<Users>


    // This property is only valid between onCreateView and
    // onDestroyView.
    //private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textDashboard
//        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }

   lateinit var adapter : MyDashboardAdapter

   private lateinit var progressDialog: ProgressDialog
   companion object {

        private var cardView: CardStackView? = null
        private var manager: CardStackLayoutManager? = null
        var topPos = 0
        fun swipe(setting: SwipeAnimationSetting) {
            manager?.setSwipeAnimationSetting(setting)
            cardView?.swipe()
        }

        fun rewind(setting: RewindAnimationSetting) {
            manager?.setRewindAnimationSetting(setting)
            cardView?.rewind()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //initialize(view)
        userArrayList = arrayListOf()
        adapter = MyDashboardAdapter(userArrayList)
        progressDialog= ProgressDialog(context)
        progressDialog.setTitle("please wait")
        progressDialog.setCanceledOnTouchOutside(false)
        getGender()

        initialize(view)
        binding.tvAppName.makeGradient(requireActivity())
        binding.ivClose.setOnClickListener {
            if (manager?.topPosition!! < adapter.itemCount) {
                val setting = SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Left)
                    .setDuration(Duration.Slow.duration)
                    .setInterpolator(AccelerateInterpolator())
                    .build()
                swipe(setting)
            }

        }

        binding.ivUndo.setOnClickListener {
            val setting = RewindAnimationSetting.Builder()
                .setDirection(Direction.Bottom)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(DecelerateInterpolator())
                .build()
            rewind(setting)

        }

        binding.ivHeart.setOnClickListener {

           try{
                val myid = FirebaseAuth.getInstance().uid
                val uid = userArrayList[manager?.topPosition!!].uid
                val user = userArrayList[manager?.topPosition!!]
                if (uid != null) {

                    val ref = FirebaseDatabase.getInstance()
                        .getReference("/users/shugas/$myid/likee/$uid")

                    ref.setValue(user)
                        .addOnSuccessListener {
                            //checkMatch(myid,user)
                            checkEmpty()
                            /* val intent= Intent(this, DashboardActivity::class.java)
                             intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                             startActivity(intent)*/
                        }
                        .addOnFailureListener {
                            Log.d("fail", "Failed ${it.message}")
                        }
                } else {
                    Log.d("fail", "Failed ")
                }

                checkMatch(myid, user)
                if (manager?.topPosition!! < adapter.itemCount) {
                    val setting = SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Right)
                        .setDuration(Duration.Slow.duration)
                        .setInterpolator(AccelerateInterpolator())
                        .build()
                    swipe(setting)
                }
            }catch (e: Exception){
               binding.rlContent.visibility = View.GONE
               binding.llBottom.visibility = View.GONE
               binding.tvEmpty.visibility = View.VISIBLE
            }
        }
        /*binding.ivNotification.onClick {
            requireActivity().launchActivity<DANotificationActivity>()
        }*/
    }
    private fun getGender() {
        val uid = FirebaseAuth.getInstance().uid
        dbref = FirebaseDatabase.getInstance().getReference("users")
        dbref.child("shugas").child("$uid").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    val user = snapshot.getValue(Users::class.java)
                    generatedUser(user)
                    Log.d("TAG", "${user!!.firstName}")

                }else{
                    binding.rlContent.visibility = View.GONE
                    binding.llBottom.visibility = View.GONE
                    binding.tvEmpty.visibility = View.VISIBLE
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }

    private fun checkMatch(myid: String?, user: Users) {
        if (user != null) {
            val uid = user.uid
            val ref = FirebaseDatabase.getInstance().getReference("/users/shugas/$uid/likee")

            ref.child("$myid").addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()){
                        val intent= Intent(context, CongratulationsActivity::class.java)
                        intent.putExtra("profileUrl", user)

                        startActivity(intent)
                    }
                else{
                    return
                }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

                    }
    }

    private fun generatedUser(user: Users?) {
        dbref = FirebaseDatabase.getInstance().getReference("users")
        //val myId = FirebaseAuth.getInstance().uid

        dbref.child("shugas").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (userSnapshot in snapshot.children){
                        val users = userSnapshot.getValue(Users::class.java)
                        if (users != null) {
                            if (users.domain != user?.domain) {
                                if(users.gender == user?.gender2){
                                    userArrayList.add(users!!)
                                    dbref.child("shugas").child("${user?.uid}").child("likee").addValueEventListener(object : ValueEventListener {

                                        override fun onDataChange(snapshot: DataSnapshot) {

                                            if (snapshot.exists()){
                                                for (userSnapshot in snapshot.children){
                                                    val userLiked = userSnapshot.getValue(Users::class.java)
                                                    userArrayList.remove(userLiked!!)
                                                    if (userArrayList.isEmpty()){
                                                        binding.rlContent.visibility = View.GONE
                                                        binding.llBottom.visibility = View.GONE
                                                        binding.tvEmpty.visibility = View.VISIBLE
                                                    }
                                                }
                                            }}

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }
                                    })

                                }
                            }
                        }
                        Log.d("users", "12e$userArrayList")
                        /*dbref.child("sponsors").addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()){
                                    for (userSnapshot in snapshot.children){
                                        val liked = userSnapshot.getValue(Users::class.java)
                                        if(liked in userArrayList){
                                            userArrayList.remove(liked!!)
                                        }
                                    }
                                    cardView!!.adapter = MyDashboardAdapter(userArrayList)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })*/
                    }
                    cardView!!.adapter = MyDashboardAdapter(userArrayList)
                    Log.d("users", "${cardView!!.adapter}")
                    if (userArrayList.isEmpty()){
                        binding.rlContent.visibility = View.GONE
                        binding.llBottom.visibility = View.GONE
                        binding.tvEmpty.visibility = View.VISIBLE
                    }
                    //checkLiked()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }



    private fun checkEmpty() {
        if (manager?.topPosition == adapter.itemCount - 1) {
            binding.rlContent.visibility = View.GONE
            binding.llBottom.visibility = View.GONE
            binding.tvEmpty.visibility = View.VISIBLE
        } else {
            binding.rlContent.visibility = View.VISIBLE
            binding.llBottom.visibility = View.VISIBLE
            binding.tvEmpty.visibility = View.GONE
        }

    }

    private fun setUser() {
        val item = userArrayList[manager!!.topPosition]
        /*this.items[item]*/
        //val item = adapter.userArrayList[manager!!.topPosition]
        binding.tvName.text = item.firstName + " , " + item.age
        binding.tvDisatance.text = item.age
        var uid= item.uid
        dbref = FirebaseDatabase.getInstance().getReference("users")
        dbref.child("shugas").child("$uid").child("about").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    val about = snapshot.getValue(User::class.java)
                    if (about != null) {
                        binding.tvProfession.text = about.work
                    }

                } else {
                    binding.tvProfession.text = "N/A"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


        //binding.tvProfession.text = item.gender2
        topPos = manager?.topPosition!!
        checkLiked(item)
    }

    private fun checkLiked(item: Users) {
            val myid = FirebaseAuth.getInstance().uid
            val uid = item.uid
            dbref = FirebaseDatabase.getInstance().getReference("users")
            dbref.child("shugas").child("$myid").child("likee").child("$uid")
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (snapshot.exists()) {
                            for (userSnapshot in snapshot.children){
                                binding.ivHeart.setColorFilter(resources.getColor(R.color.shuga_red))
                                /*if (userArrayList.isEmpty()){
                                    binding.rlContent.visibility = View.GONE
                                    binding.llBottom.visibility = View.GONE
                                    binding.tvEmpty.visibility = View.VISIBLE
                                }*/
                            }

                            /*binding.ivHeart.setColorFilter(
                                ContextCompat.getColor(
                                    view!!.context,
                                    R.color.shuga_red
                                )
                            )*/
                            //binding.ivHeart.setColorFilter(ContextCompat.getColor(view!!.context, R.color.shuga_red))
                        } else {
                            return

                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }


                })

        }
    private var isPause = false

    private fun initialize(view: View) {
        progressDialog.dismiss()
        manager = CardStackLayoutManager(requireActivity(), object : CardStackListener {
            override fun onCardDragging(direction: Direction, ratio: Float) {
            }

            override fun onCardSwiped(direction: Direction) {
                if (direction == Direction.Right && !isPause) {
                    isPause = true
                    activity!!.launchActivity<CongratulationsActivity> { }
                }

            }

            override fun onCardAppeared(view: View, position: Int) {
                setUser()

            }

            override fun onCardDisappeared(view: View, position: Int) {
                checkEmpty()

            }

            override fun onCardRewound() {
            }

            override fun onCardCanceled() {
            }


        })
        manager?.setStackFrom(StackFrom.None)
        manager?.setVisibleCount(3)
        manager?.setTranslationInterval(8.0f)
        manager?.setScaleInterval(0.95f)
        manager?.setSwipeThreshold(0.3f)
        manager?.setMaxDegree(60.0f)
        manager?.setDirections(Direction.HORIZONTAL)
        manager?.setCanScrollHorizontal(false)
        manager?.setCanScrollVertical(false)
        manager?.cardStackListener
        manager?.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager?.setOverlayInterpolator(LinearInterpolator())
       /* adapter = RecyclerViewAdapter(
            R.layout.da_item_user,
            onBind = { view: View, user: User, i: Int ->
                view.ivDateProfile.setImageResource(user.img!!)

            })*/
        /*adapter.onItemClick = { i: Int, view: View, user: Users ->
            var intent = Intent(activity, UserDetailActivity::class.java);
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                requireActivity(),
                view.ivDateProfile as View,
                "profile"
            )
            startActivityForResult(intent, 101, options.toBundle())
            isPause = true

        }*/
        cardView = view.findViewById(R.id.cardStack)
        cardView?.layoutManager = manager
        cardView?.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        isPause = true

        /*  manager?.topPosition = topPos
          setUser()*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //  binding = null
    }
}