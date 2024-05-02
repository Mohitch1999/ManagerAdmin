package com.example.managerapp.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import com.example.managerapp.R
import com.example.managerapp.databinding.ActivitySplashBinding
import kotlinx.coroutines.delay

class SplashActivity : AppCompatActivity() {

    lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logoImageView.visibility = View.GONE
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in_for_splash)
        Handler().postDelayed({
            binding.logoImageView.visibility = View.VISIBLE

            binding.logoImageView.startAnimation(animation)
        }, 500)


        // Start the animation


        Handler().postDelayed({
            val intent = Intent(this, AdminLoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)


    }
}