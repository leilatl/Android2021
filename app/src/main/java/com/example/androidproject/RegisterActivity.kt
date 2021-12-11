package com.example.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.androidproject.firestore.FirestoreClass
import com.example.androidproject.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.tv_register
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setupActionBar()

        tv_login.setOnClickListener{
            onBackPressed()
        }
        btn_register.setOnClickListener{
            registerUser()
        }
    }

    private fun setupActionBar(){
        val toolbar = findViewById<Toolbar>(R.id.toolbar_register)
        setSupportActionBar(toolbar)


        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
            actionBar.title = "Register"
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }

    }

    private fun checkRegistration(): Boolean{
        return when{
            TextUtils.isEmpty(et_name.text.toString().trim() {it <= ' '}) -> {
                showErrorSnackBar("Enter Name", true)
                false
            }
            TextUtils.isEmpty(et_surname.text.toString().trim() {it <= ' '}) -> {
                showErrorSnackBar("Enter Surname", true)
                false
            }
            TextUtils.isEmpty(et_email.text.toString().trim() {it <= ' '}) -> {
                showErrorSnackBar("Enter Email", true)
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim() {it <= ' '}) -> {
                showErrorSnackBar("Enter Password", true)
                false
            }
            TextUtils.isEmpty(et_confirm.text.toString().trim() {it <= ' '}) -> {
                showErrorSnackBar("Enter Password twice", true)
                false
            }
            et_password.text.toString().trim() {it <= ' '} != et_confirm.text.toString().trim() {it <= ' '} ->{
                showErrorSnackBar("Passwords dont match", true)
                false
            }
            !terms_checkbox.isChecked ->{
                showErrorSnackBar("Read Terms", true)
                false
            }
            else -> {
                //showErrorSnackBar("Everything is ok!", false)
                true
            }

        }
    }

    private fun registerUser(){
        if (checkRegistration()){
            val email: String = et_email.text.toString().trim() {it <= ' '}
            val password: String = et_password.text.toString().trim() {it <= ' '}

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                OnCompleteListener<AuthResult> { task ->
                    if(task.isSuccessful){
                        val firebaseUser: FirebaseUser = task.result!!.user!!

                        val user = User(
                            firebaseUser.uid,
                            et_name.text.toString().trim {it <= ' '},
                            et_surname.text.toString().trim {it <= ' '},
                            et_email.text.toString().trim {it <= ' '},
                        )
                        FirestoreClass().registerUser(this@RegisterActivity, user)
                    }else{
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
            )
        }
    }

    fun registrationSuccess(){
        Toast.makeText(
            this@RegisterActivity,
            "success",
            Toast.LENGTH_SHORT
        ).show()
        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
        finish()
    }


}