package com.usalama.usalamatechnology.shuga.activity

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.usalama.usalamatechnology.shuga.BaseActivity
import com.usalama.usalamatechnology.shuga.R
import com.usalama.usalamatechnology.shuga.databinding.ActivitySignUpProfilePicBinding
import com.usalama.usalamatechnology.shuga.models.Shugas

import com.usalama.usalamatechnology.shuga.models.User
import com.usalama.usalamatechnology.shuga.utils.launchActivity
import com.usalama.usalamatechnology.shuga.utils.onClick
import kotlinx.android.synthetic.main.activity_sign_up_details.*
import kotlinx.android.synthetic.main.activity_sign_up_profile_pic.*
import kotlinx.android.synthetic.main.activity_sign_up_profile_pic.continueBtn
import java.text.SimpleDateFormat
import java.util.*

class SignUpProfilePicActivity : BaseActivity() {
    private lateinit var binding: ActivitySignUpProfilePicBinding
    lateinit var imageView: ImageView
    lateinit var button: Button
    lateinit var storageReference: StorageReference
    private lateinit var database : DatabaseReference
    private lateinit var progressDialog: ProgressDialog
    companion object {
        val TAG = "ProfileActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_profile_pic)

        binding = ActivitySignUpProfilePicBinding.inflate(layoutInflater)

        imageView = findViewById(R.id.imageView)
        button = findViewById(R.id.buttonLoadPicture)
        button.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
        }

        continueBtn.setOnClickListener{
            uploadImageToFirebase()

            /*launchActivity<SubscriptionActivity> { }*/ }
    }
    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            selectedPhotoUri = data?.data
            imageView.setImageURI(selectedPhotoUri)
            button.alpha=0f
        }
    }
    private fun uploadImageToFirebase() {

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading File....")
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")



        storageReference.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")

                Toast.makeText(this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                if (progressDialog.isShowing) {
                    progressDialog.dismiss()
                }


                storageReference.downloadUrl.addOnSuccessListener {
                    progressDialog = ProgressDialog(this)
                    progressDialog.setTitle("Uploading profile....")
                    progressDialog.show()
                    val domain = intent.getStringExtra("domain")
                    val uid = FirebaseAuth.getInstance().uid?:""
                    val firstName=intent.getStringExtra("firstName")
                    val lastName=intent.getStringExtra("lastName")
                    val gender=intent.getStringExtra("gender")
                    val age=intent.getStringExtra("age")
                    val dob=intent.getStringExtra("dob")
                    val sugar =intent.getStringExtra("sugar")
                    val gender2=intent.getStringExtra("gender2")
                    val profileUrl= "$it"


                    if (firstName!= null||lastName!= null||age!= null||dob!= null){
                        val ref = FirebaseDatabase.getInstance().getReference("/users/shugas/$uid")
                        val Shuga = Shugas(uid,firstName,lastName,domain,gender,profileUrl,gender2,dob,age,sugar)

                        ref.setValue(Shuga)
                            .addOnSuccessListener {
                                if (progressDialog.isShowing) {
                                    progressDialog.dismiss()
                                }

                                val intent=Intent(this, SubscriptionActivity::class.java)
                                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)

                            }
                            .addOnFailureListener {
                                Log.d(TAG,"Failed ${it.message}")
                            }
                    }else{
                        Toast.makeText(this, "Empty Fields",Toast.LENGTH_SHORT).show()
                    }

            }
            .addOnFailureListener {
                if (progressDialog.isShowing) {
                    progressDialog.dismiss()
                }
                Toast.makeText(this, "Failed to Upload", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Failed to upload image to storage: ${it.message}")
            }

    }

    /*private fun saveUserToFirebaseDatabase(profileUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        database = FirebaseDatabase.getInstance().getReference("Users")
        val user = Sponsors(uid, profileUrl)
        database.child(uid).setValue(user).addOnSuccessListener {

            binding.imageView.setImageURI(null)

            Toast.makeText(this,"Successfully Saved",Toast.LENGTH_SHORT).show()
            val intent= Intent(this, SignUpDetailsActivity::class.java)
            intent.putExtra("samplename", "abd")
            startActivity(intent)

        }.addOnFailureListener{

            Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()

        }

    }*/
}}