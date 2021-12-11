package com.example.androidproject

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_register.*
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.core.app.ActivityCompat
import com.example.androidproject.firestore.FirestoreClass
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.IOException

class ProfileActivity : BaseActivity(), View.OnClickListener {
    private var mSelectedImageFileUri: Uri? = null
    private var mUserProfileImageURL: String =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setUpActionBar()
        val sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val firstName = sharedPreferences.getString("firstName", "")!!
        profile_name.hint = firstName
        val lastName = sharedPreferences.getString("lastName", "")!!
        profile_surname.hint = lastName
        val email = sharedPreferences.getString("email", "")!!
        profile_email.hint = email
        var gender: String = "male"
        btn_female.setOnClickListener {
            btn_male.setBackgroundResource(R.drawable.white_btn)
            btn_male.setTextColor(Color.parseColor("#973399"))
            btn_female.setBackgroundResource(R.drawable.btn_style)
            btn_female.setTextColor(Color.parseColor("#FFFFFF"))
            gender = "female"
        }
        btn_male.setOnClickListener {
            btn_female.setBackgroundResource(R.drawable.white_btn)
            btn_female.setTextColor(Color.parseColor("#973399"))
            btn_male.setBackgroundResource(R.drawable.btn_style)
            btn_male.setTextColor(Color.parseColor("#FFFFFF"))
            gender = "male"
        }
        iv_user_photo.setOnClickListener(this@ProfileActivity)
        btn_save.setOnClickListener {
            if(mSelectedImageFileUri != null){
                FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri, "user_image")
            }

            val userHashmap = HashMap<String, Any>()
            val mobileNum = profile_number.text.toString().trim{it <= ' '}
            if(mobileNum.isNotEmpty()){
                userHashmap["mobile"] = mobileNum.toLong()
            }
            if(mUserProfileImageURL.isNotEmpty()){
                showErrorSnackBar("hello", true)
                userHashmap["image"] = mUserProfileImageURL
            }
            userHashmap["gender"] = gender
            userHashmap["profileCompleted"] = true
            FirestoreClass().updateUserProfileData(this, userHashmap)
        }
    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.iv_user_photo -> {
                    if(ContextCompat.checkSelfPermission(
                            this, Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                    ){
                        Constants.showImageChooser(this)
                    }else{
                        ActivityCompat.requestPermissions(
                            this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            2
                        )
                    }


                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 2){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)
            }else{

            }
        }
    }
    private fun setUpActionBar(){
        setSupportActionBar(toolbar_profile)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
            actionBar.title = "Profile"
        }
        toolbar_profile.setNavigationOnClickListener{onBackPressed()}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == 1){
                if(data != null){
                    try{
                        mSelectedImageFileUri = data.data!!
                        iv_user_photo.setImageURI(Uri.parse(mSelectedImageFileUri.toString()))
                    }catch (e: IOException){
                        e.printStackTrace()
                    }
                }
            }
        }
    }
    fun userProfileUpdateSuccess(){
        val intent = Intent(this@ProfileActivity, MainActivity::class.java)
        startActivity(intent)

    }
    fun imageUploadSuccess(imageURL: String){
        mUserProfileImageURL = imageURL
    }
}