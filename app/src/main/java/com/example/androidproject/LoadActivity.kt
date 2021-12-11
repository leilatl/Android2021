package com.example.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class LoadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load)
        Handler(Looper.getMainLooper()).postDelayed(
            {
                startActivity(Intent(this@LoadActivity, LoginActivity::class.java))
                finish()
            },
            2500
        )
    }


}