package com.example.teaqualitykotlin.ui.splashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.teaqualitykotlin.MainActivity
import com.example.teaqualitykotlin.R
import com.example.teaqualitykotlin.databinding.ActivitySplashScreenBinding
import com.example.teaqualitykotlin.ui.authorization.SignInActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appIconImageView.alpha = 0f
        binding.appIconImageView.animate().setDuration(1500).alpha(1f).withEndAction {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}