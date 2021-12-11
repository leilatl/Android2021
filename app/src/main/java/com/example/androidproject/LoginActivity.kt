package com.example.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.example.androidproject.firestore.FirestoreClass
import com.example.androidproject.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        tv_register.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        btn_login.setOnClickListener {
            logUserIn()
        }
        tv_forgot_password.setOnClickListener {
            val intent = Intent(this@LoginActivity, ResetPasswordActivity::class.java)
            startActivity(intent)
        }
    }
    private fun checkLoginDetails():Boolean{
        return when{
            TextUtils.isEmpty(et_login_name.text.toString().trim() {it <= ' '}) -> {
                showErrorSnackBar("Enter Email", true)
                false
            }
            TextUtils.isEmpty(et_login_password.text.toString().trim() {it <= ' '}) -> {
                showErrorSnackBar("Enter Password", true)
                false
            }
            else -> {
                true
            }
        }
    }
    private fun logUserIn(){
        if (checkLoginDetails()){
            val email: String = et_login_name.text.toString().trim() {it <= ' '}
            val password: String = et_login_password.text.toString().trim() {it <= ' '}
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener {
                task ->
                if (task.isSuccessful){
                    showErrorSnackBar("Success", false)
                    FirestoreClass().getUserDetails(this@LoginActivity)
                }else{
                    showErrorSnackBar(task.exception!!.message.toString(), true)
                }
            }
        }
    }
    fun userLoggedInSuccess(user: User){
        Log.i("Name ", user.firstName)

        if(user.profileCompleted){
            val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
            startActivity(intent)
        }else{
            val intent = Intent(this@LoginActivity, ProfileActivity::class.java)
            intent.putExtra("extra_user_details", user)
            startActivity(intent)
        }

        finish()
    }
}