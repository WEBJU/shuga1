package com.usalama.usalamatechnology.shuga.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import com.usalama.usalamatechnology.shuga.BaseActivity
import com.usalama.usalamatechnology.shuga.R
import com.usalama.usalamatechnology.shuga.activity.ui.dashboard.DashboardFragment
import com.usalama.usalamatechnology.shuga.activity.ui.dashboard.MyDashboardAdapter
import com.usalama.usalamatechnology.shuga.databinding.ActivityUserDetailBinding
import com.usalama.usalamatechnology.shuga.databinding.ActivityViewProfileBinding
import com.usalama.usalamatechnology.shuga.models.DetailsAdapter
import com.usalama.usalamatechnology.shuga.models.Pics
import com.usalama.usalamatechnology.shuga.models.User
import com.usalama.usalamatechnology.shuga.models.Users
import com.usalama.usalamatechnology.shuga.utils.*
import com.usalama.usalamatechnology.shuga.utils.cardstackview.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_user_detail.*
import kotlinx.android.synthetic.main.da_content_user_detail.*
import kotlinx.android.synthetic.main.da_item_user_detail.*
import kotlinx.android.synthetic.main.da_item_user_detail.view.*
import kotlinx.android.synthetic.main.fragment_profile.*


class UserDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var dbref : DatabaseReference
    var carouselView : CarouselView? = null
    var imageArray : ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkUser()
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        makeNormalStatusBar()
        makeTransaprant()
        /*binding.ivPhoto.visibility= View.VISIBLE
        binding.ivPhoto2.visibility = View.GONE*/
        val user = intent.getParcelableExtra<Users>("detail")
        val pic = intent.getStringExtra("pic")
        //Picasso.get().load(user!!.profileUrl!!).into(binding.ivPhoto)
        binding.tvName.text = user!!.firstName
        binding.tvProfession.text = user!!.gender
        binding.tvAbout.text = "Age: "+ user!!.age+"\n interested in: " + user!!.gender2
        val uid =user.uid

        imageArray.add(user!!.profileUrl!!)
        if (pic != null) {
            imageArray.add(pic!!.toString())
        }else{
            imageArray.add("https://thumbs.dreamstime.com/z/no-image-available-icon-flat-vector-no-image-available-icon-flat-vector-illustration-132482953.jpg")
        }




        Log.d("TAG","Failed ${pic}")
        carouselView = findViewById(R.id.carouselView)

        carouselView!!.pageCount = imageArray.size

        carouselView!!.setImageListener(imageListener)





        /*ivPhoto.setOnClickListener {
            binding.ivPhoto2.visibility = View.VISIBLE
            binding.ivPhoto.visibility = View.GONE
            dbref = FirebaseDatabase.getInstance().getReference("users")

            dbref.child("shugas").child("$uid").child("pictures").addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()){
                        val about = snapshot.getValue(Pics::class.java)
                        if (about != null) {
                            Picasso.get().load(about.img1).into(ivPhoto2)
                            ivPhoto2.setOnClickListener {
                                binding.ivPhoto.visibility = View.VISIBLE
                                binding.ivPhoto2.visibility = View.GONE
                            }
                        } else{
                            Picasso.get().load("https://thumbs.dreamstime.com/z/no-image-available-icon-flat-vector-no-image-available-icon-flat-vector-illustration-132482953.jpg").into(ivPhoto2)
                        }

                    }else{

                        Picasso.get().load("https://thumbs.dreamstime.com/z/no-image-available-icon-flat-vector-no-image-available-icon-flat-vector-illustration-132482953.jpg").into(ivPhoto2)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            })
        }*/
        dbref = FirebaseDatabase.getInstance().getReference("users")

        dbref.child("shugas").child("$uid").child("about").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    val about = snapshot.getValue(User::class.java)
                    if (about != null) {
                        binding.tvMore.text = about.work
                        binding.tvMoreAbout.text = about.status
                    }

                }else{
                    binding.tvMore.text = "N/A"
                    binding.tvMoreAbout.text = "N/A"
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })


        binding.ivBack.onClick {
            onBackPressed()
        }
    }

    private fun getUsers(uid: String?) {

    }

    var imageListener = ImageListener{position, imageView -> Picasso.get().load(imageArray[position]).into(imageView)}

    private fun checkUser() {

        /*val uid = FirebaseAuth.getInstance().uid
        dbref = FirebaseDatabase.getInstance().getReference("users")

        dbref.child("sponsors").child(uid!!).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    val user = snapshot.getValue(Users::class.java)
                    Log.d("TAG", "${user!!.firstName}")
                    Picasso.get().load(user!!.profileUrl!!).into(binding.ivPhoto)
                    binding.tvName.text = user!!.firstName
                    binding.tvProfession.text = user!!.gender
                    binding.tvAbout.text = "Year of birth: "+ user!!.age+"\n interested in: " + user!!.gender2
                    *//*for (userSnapshot in snapshot.children){


                        val user = userSnapshot.getValue(Users::class.java)
                        Log.d("TAG", "$user")

                    }*//*


                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })*/
    }
    /*private lateinit var binding: ActivityUserDetailBinding
    private lateinit var dbref : DatabaseReference
    private lateinit var userArrayList : ArrayList<Users>
    lateinit var adapter : DetailsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userdetail = intent.getParcelableExtra<Users>("detail")
        //Picasso.get().load(userdetail?.profileUrl).into(ivDateProfilez)
        userArrayList = arrayListOf()
        adapter = DetailsAdapter(userArrayList)

        //generatedUser()
        initialize()


        binding.ivClose.setOnClickListener {
            if (manager?.topPosition!! < adapter.itemCount) {
                val setting = SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Left)
                    .setDuration(Duration.Slow.duration)
                    .setInterpolator(AccelerateInterpolator())
                    .build()
                DashboardFragment.swipe(setting)
            }

        }

        binding.ivUndo.setOnClickListener {

            val setting = RewindAnimationSetting.Builder()
                .setDirection(Direction.Bottom)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(DecelerateInterpolator())
                .build()
            DashboardFragment.rewind(setting)

        }

        binding.ivHeart.setOnClickListener {
            Log.d(
                "item", "${userArrayList[manager?.topPosition!!].uid}"
            )
            val myid = FirebaseAuth.getInstance().uid
            val uid = userArrayList[manager?.topPosition!!].uid
            val user= userArrayList[manager?.topPosition!!]
            if (uid != null) {

                val ref = FirebaseDatabase.getInstance().getReference("/users/sponsors/$myid/likee/$uid")

                ref.setValue(user)
                    .addOnSuccessListener {
                        Log.d("fail","i gar jokes ")
                        *//* val intent= Intent(this, DashboardActivity::class.java)
                         intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                         startActivity(intent)*//*
                    }
                    .addOnFailureListener {
                        Log.d("fail","Failed ${it.message}")
                    }
            }else{
                Log.d("fail","Failed ")
            }
            //checkMatch(myid,user)
            if (manager?.topPosition!! < adapter.itemCount) {
                val setting = SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Right)
                    .setDuration(Duration.Slow.duration)
                    .setInterpolator(AccelerateInterpolator())
                    .build()
                DashboardFragment.swipe(setting)
            }
        }
        //initialize(userdetail)
        binding.ivBack.onClick {
            onBackPressed()
        }

    }

    private fun generatedUser() {
        TODO("Not yet implemented")
    }

    private  var fromExplore:Boolean=false
    private var manager: CardStackLayoutManager? = null
    private fun setUser() {
        var user = intent.getParcelableExtra<Users>("detail")
        //val item = adapter.getItem(manager!!.topPosition)
      Log.d("users", "1$user")
        if (user != null) {
            tvName.text = user.firstName
            binding.userDetail.tvDisatance.text = user.age
            binding.userDetail.tvProfession.text = user.gender
            DashboardFragment.topPos=manager?.topPosition!!
        }


    }
    private fun checkEmpty() {
        if (manager?.topPosition == adapter.itemCount - 1) {
            binding.userDetail.rlContent.visibility = View.GONE
            binding.llBottom.visibility = View.GONE
            finish()
        } else {
            binding.userDetail.rlContent.visibility = View.VISIBLE
            binding.llBottom.visibility = View.VISIBLE

        }

    }
    private var isProcessing=false
    private fun initialize() {


        fromExplore=intent.getBooleanExtra("from_explore",false)
        manager = CardStackLayoutManager(this, object : CardStackListener {
            override fun onCardDragging(direction: Direction, ratio: Float) {
            }

            override fun onCardSwiped(direction: Direction) {
                Log.e("cardSwiped",direction.name)
                if (direction==Direction.Right){
                    launchActivity<CongratulationsActivity> {  }
                }
            }

            override fun onCardAppeared(view: View, position: Int) {
                //setUser()
                var user = intent.getParcelableExtra<Users>("detail")
                Picasso.get().load(user?.profileUrl!!).into(view.ivDateProfilez)
                Log.d("first", "me$user")
                if (user != null) {
                    binding.userDetail.tvName.text = user?.firstName
                    binding.userDetail.tvDisatance.text = user.age
                    binding.userDetail.tvProfession.text = user.gender
                    DashboardFragment.topPos=manager?.topPosition!!
                }

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
        *//*adapter = RecyclerViewAdapter(
            R.layout.da_item_user_detail,
            onBind = { view: View, user: Users, i: Int ->
                Log.d("list", "254$user")
                Picasso.get().load(user.profileUrl!!).into(view.ivDateProfilez)
                //view.ivDateProfilez.setImageResource(user.img!!)


            })*//*
        binding.cardStack.layoutManager = manager
        binding.cardStack.adapter = adapter
        binding.cardStack.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
        *//*if (fromExplore){
            adapter.addItems(generateList())
            manager?.topPosition= intent.getIntExtra("pos",0)

        }else{

            adapter.addItems(generateUser())
            manager?.topPosition= DashboardFragment.topPos
        }*//*

        binding.ivClose.setOnClickListener {
            if (manager?.topPosition!! < adapter.itemCount) {
                val setting = SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Left)
                    .setDuration(Duration.Slow.duration)
                    .setInterpolator(AccelerateInterpolator())
                    .build()
                manager?.setSwipeAnimationSetting(setting)
                if (!fromExplore){
                    DashboardFragment.swipe(setting)
                }
                binding.cardStack.swipe()
            }
        }

        binding.ivUndo.setOnClickListener {

            val setting = RewindAnimationSetting.Builder()
                .setDirection(Direction.Bottom)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(DecelerateInterpolator())
                .build()
            manager?.setRewindAnimationSetting(setting)
            if (!fromExplore){
                DashboardFragment.rewind(setting)

            }
            binding.cardStack.rewind()

        }

        binding.ivHeart.setOnClickListener {
            if (manager?.topPosition!! < adapter.itemCount && !isProcessing) {
                isProcessing=true
                val setting = SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Right)
                    .setDuration(Duration.Slow.duration)
                    .setInterpolator(AccelerateInterpolator())
                    .build()
                manager?.setSwipeAnimationSetting(setting)
                if (!fromExplore){
                    DashboardFragment.swipe(setting)
                }
                binding.cardStack.swipe()

            }
        }
    }


    override fun onResume() {
        super.onResume()
        isProcessing=false
    }
    *//*  private fun checkEmpty() {
          if (manager?.topPosition == adapter.itemCount - 1) {
              rlContent.visibility = View.GONE
              llBottom.visibility = View.GONE
              tvEmpty.visibility = View.VISIBLE
          } else {
              rlContent.visibility = View.VISIBLE
              llBottom.visibility = View.VISIBLE
              tvEmpty.visibility = View.GONE
          }

      }

      private fun setUser() {
          val item = adapter.getItem(manager!!.topPosition)
          tvName.text = item.name
          tvDisatance.text = item.distance
          tvProfession.text = item.proffesion
      }
  */
}
