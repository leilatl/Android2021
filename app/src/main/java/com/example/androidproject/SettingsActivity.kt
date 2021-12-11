package com.example.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidproject.firestore.FirestoreClass
import com.example.androidproject.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    private lateinit var mUserDetail: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setUpActionBar()
        getUserDetails()
        btn_logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        btn_settings_edit.setOnClickListener {
            val intent = Intent(this@SettingsActivity, ProfileActivity::class.java)
            intent.putExtra("user_details", mUserDetail)
            startActivity(intent)
        }
    }
    private fun setUpActionBar(){
        setSupportActionBar(toolbar_settings)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }
        toolbar_settings.setNavigationOnClickListener{onBackPressed()}
    }
    private fun getUserDetails() {

        // Call the function of Firestore class to get the user details from firestore which is already created.
        FirestoreClass().getUserDetails(this@SettingsActivity)
    }
    fun userDetailsSuccess(user: User) {
        mUserDetail = user

        GlideLoader(this@SettingsActivity).loadUserPicture(user.image, iv_settings_user_photo)


        tv_name.text = "${user.firstName} ${user.lastName}"
        tv_email.text = user.email
        tv_mobile.text = "${user.mobile}"


    }

}