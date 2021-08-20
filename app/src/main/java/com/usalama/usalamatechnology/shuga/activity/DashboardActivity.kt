package com.usalama.usalamatechnology.shuga.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.usalama.usalamatechnology.shuga.BaseActivity
import com.usalama.usalamatechnology.shuga.R
import com.usalama.usalamatechnology.shuga.activity.ui.ProfileFragment
import com.usalama.usalamatechnology.shuga.activity.ui.SubscriptionFragment
import com.usalama.usalamatechnology.shuga.activity.ui.dashboard.DashboardFragment
import com.usalama.usalamatechnology.shuga.activity.ui.home.HomeFragment
import com.usalama.usalamatechnology.shuga.activity.ui.notifications.NotificationsFragment
import com.usalama.usalamatechnology.shuga.databinding.ActivityDashboardBinding
import com.usalama.usalamatechnology.shuga.models.Users
import com.usalama.usalamatechnology.shuga.utils.*

class DashboardActivity : BaseActivity() {
    companion object {
        var currentUser: Users? = null
        val TAG = "LatestMessages"
    }
    private var backPressedTime =0L
    private lateinit var binding: ActivityDashboardBinding
    var homeFragment = DashboardFragment()
    var exploreFragment = HomeFragment()
    var messageFragment = NotificationsFragment()
    var profileFragment = ProfileFragment()
    var subscriptionFragment = SubscriptionFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        verifyUserIsLoggedIn()
        currUser()
        //  lightStatusBar(color(R.color.da_white))
        replaceFragment(homeFragment, R.id.frameContainer)

        val navView: BottomNavigationView = binding.navView

//        val navController = findNavController(R.id.nav_host_fragment_activity_dashboard)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
        navView.setSelectedItemId(R.id.navigation_home);

        navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(homeFragment, R.id.frameContainer)

                }
                R.id.navigation_matches -> {
                    replaceFragment(exploreFragment, R.id.frameContainer)

                }
                R.id.navigation_messages -> {
                    replaceFragment(messageFragment, R.id.frameContainer)

                }
                R.id.subscription -> {
                    replaceFragment(subscriptionFragment, R.id.frameContainer)
                }
                R.id.profile -> {
                    replaceFragment(profileFragment, R.id.frameContainer)
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
        navView.itemIconTintList = null

    }

    override fun onBackPressed() {
           if (backPressedTime + 3000 > System.currentTimeMillis()){
               super.onBackPressed()
           }else{
               Toast.makeText(applicationContext,"Press back again to exit", Toast.LENGTH_SHORT).show()
               backPressedTime= System.currentTimeMillis()
           }
       }
    private fun currUser() {
        val uid = FirebaseAuth.getInstance().uid?:intent.getStringExtra("uid")
        val ref = FirebaseDatabase.getInstance().getReference("/users/shugas/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(Users::class.java)
                Log.d("LatestMessages", "Current user ${currentUser?.profileUrl}")
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }


    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid?:intent.getStringExtra("uid")

        if(uid==null){
            val intent= Intent(this, Welcome::class.java)
            intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }
}