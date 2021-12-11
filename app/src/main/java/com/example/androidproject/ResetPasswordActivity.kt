package com.example.androidproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        setupActionBar()
        btn_submit.setOnClickListener {
            val email: String = et_reset_email.text.toString().trim {it <= ' '}
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener{
                Toast.makeText(this@ResetPasswordActivity,
                "check email", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun setupActionBar(){
        val toolbar = findViewById<Toolbar>(R.id.toolbar_reset_password)
        setSupportActionBar(toolbar)


        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
            actionBar.title = "Reset password"
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }

    }
}