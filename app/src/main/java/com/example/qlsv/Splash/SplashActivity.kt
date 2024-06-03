package com.example.qlsv.Splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
//import com.example.qlsv.Account.Login.LoginActivity
import com.example.qlsv.Account.Login.LoginActivity
import com.example.qlsv.MainActivity
import com.example.qlsv.databinding.ActivitySplashBinding
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val SPLASH_DELAY = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUser = auth.currentUser
        Handler().postDelayed({
            val intent = when {
                currentUser != null && currentUser.isEmailVerified -> Intent(
                    this,
                    MainActivity::class.java
                )

                else -> Intent(this, LoginActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, SPLASH_DELAY)
    }
}