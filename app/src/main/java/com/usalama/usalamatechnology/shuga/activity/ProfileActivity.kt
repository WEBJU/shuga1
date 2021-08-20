package com.usalama.usalamatechnology.shuga.activity

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.usalama.usalamatechnology.shuga.BaseActivity
import com.usalama.usalamatechnology.shuga.R
import com.usalama.usalamatechnology.shuga.databinding.ActivityProfileBinding
import com.usalama.usalamatechnology.shuga.models.Pics
import com.usalama.usalamatechnology.shuga.models.Shugas
import com.usalama.usalamatechnology.shuga.models.User
import com.usalama.usalamatechnology.shuga.models.Users
import com.usalama.usalamatechnology.shuga.utils.invalidateButton
import com.usalama.usalamatechnology.shuga.utils.onClick
import com.usalama.usalamatechnology.shuga.utils.updateGenderButton
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.da_item_addphoto.*
import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : BaseActivity(), TextWatcher {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var database : DatabaseReference
    lateinit var button: Button
    private lateinit var dateofbirth : String
    val today = Calendar.getInstance()
    lateinit var storageReference: StorageReference
    private lateinit var progressDialog: ProgressDialog
    private fun showAddPhotoDialog() {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(0))
        dialog.setContentView(R.layout.da_item_addphoto)
        dialog.window?.setLayout(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.tvCamera.onClick {
            // launchActivity<DACameraActivity>()
        }
        dialog.tvCameraRoll.onClick {
            // launchActivity<DACameraActivity>()
        }
        dialog.show()

    }


    private var cardClickListener = View.OnClickListener { showAddPhotoDialog() }
    var selectedPhotoUri: Uri? = null
    //private var selected: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*binding.edtUsername.addTextChangedListener(this)
        binding.edtAboutMe.addTextChangedListener(this)
        binding.cardProfile1.setOnClickListener(cardClickListener)*/
        /*binding.cardProfile2.setOnClickListener(cardClickListener)
        binding.cardProfile3.setOnClickListener(cardClickListener)
        binding.cardProfile4.setOnClickListener(cardClickListener)
        binding.cardProfile5.setOnClickListener(cardClickListener)
        binding.cardProfile6.setOnClickListener(cardClickListener)*/
       /* binding.ivFemale.setOnClickListener(genderClickListener)
        binding.ivMale.setOnClickListener(genderClickListener)
        binding.ivOther.setOnClickListener(genderClickListener)*/

        /*binding.ivMale.performClick()
        binding.ivBack.onClick {
            onBackPressed()
        }*/

        val datePicker = findViewById<DatePicker>(R.id.date_Picker)
        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)

        ) { view, year, month, day ->
            val month = month + 1
            dateofbirth = "$day/$month/$year"
            val intent=Intent(this, ProfileActivity::class.java)
            intent.putExtra("details", "$dateofbirth")

        }
        //Log.d("first", "$dateofbirth")
        ivEdit.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
        }
        ivEdit2.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 200)
        }
        ivEdit3.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 300)
        }

        binding.btnSave.onClick {
            uploadImageToFirebaseStorage()
        }
    }

    private fun uploadImageToFirebaseStorage() {

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading Files....")
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")



        storageReference.putFile(selectedPhotoUri!!,)
            .addOnSuccessListener {
                Log.d(SignUpProfilePicActivity.TAG, "Successfully uploaded image: ${it.metadata?.path}")

                Toast.makeText(this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                if (progressDialog.isShowing) {
                    progressDialog.dismiss()
                }


                storageReference.downloadUrl.addOnSuccessListener {
                    progressDialog = ProgressDialog(this)
                    progressDialog.setTitle("Uploading snaps....")
                    progressDialog.show()
                    val uid = FirebaseAuth.getInstance().uid?:""
                    val profileUrl= "$it"


                    if (profileUrl!= null){
                        val ref = FirebaseDatabase.getInstance().getReference("/users/shugas/$uid/pictures")
                        val Shuga= Pics(uid,profileUrl)

                        ref.setValue(Shuga)
                            .addOnSuccessListener {
                                if (progressDialog.isShowing) {
                                    progressDialog.dismiss()
                                }
                                uploadDetails()
                                try{
                                    val intent = Intent(this, ViewProfileActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                }catch (e: NullPointerException){
                                    finish()
                                }

                            }
                            .addOnFailureListener {
                                Log.d("TAG","Failed ${it.message}")
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
                        Log.d(SignUpProfilePicActivity.TAG, "Failed to upload image to storage: ${it.message}")
                    }

            }}

    private fun uploadDetails() {

        val uid = FirebaseAuth.getInstance().uid
        val username = binding.edtUsername.text.toString()
        val phone = binding.edtMobileNum.text.toString()
        val status = binding.edtAboutMe.text.toString()
        val location = binding.edtLocation.text.toString()
        val work = binding.edtWork.text.toString()
        val birthday = intent.getStringExtra("details")

        database = FirebaseDatabase.getInstance().getReference("users")
        val User = User(username,dateofbirth,location,work,phone,status)
        if (uid != null&&username.isNotEmpty()&&phone.isNotEmpty()&&location.isNotEmpty()&&work.isNotEmpty()&&status.isNotEmpty()) {
            database.child("shugas").child(uid).child("about").setValue(User).addOnSuccessListener {

                binding.edtUsername.text.clear()
                binding.edtMobileNum.text.clear()
                binding.edtAboutMe.text.clear()
                binding.edtLocation.text.clear()
                binding.edtWork.text.clear()

                Toast.makeText(this@ProfileActivity,"Successfully Saved",Toast.LENGTH_SHORT).show()
                val intent= Intent(this@ProfileActivity, DashboardActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }.addOnFailureListener{

                Toast.makeText(this@ProfileActivity,"Failed",Toast.LENGTH_SHORT).show()


            }
        }else{
            Toast.makeText(this@ProfileActivity, "Empty Fields",Toast.LENGTH_SHORT).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            selectedPhotoUri = data?.data
            img.setImageURI(selectedPhotoUri)
        }
        else if (resultCode == RESULT_OK && requestCode == 200) {
            selectedPhotoUri = data?.data
            img2.setImageURI(selectedPhotoUri)

        }else if (resultCode == RESULT_OK && requestCode == 300) {
            selectedPhotoUri = data?.data
            img3.setImageURI(selectedPhotoUri)
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        TODO("Not yet implemented")
        invalidateButton(p0.toString().isNotEmpty(), binding.btnSave)

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        TODO("Not yet implemented")
    }

    override fun afterTextChanged(p0: Editable?) {
        TODO("Not yet implemented")
    }
}