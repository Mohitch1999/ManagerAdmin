package com.example.managerapp.view.fragment.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.managerapp.R
import com.example.managerapp.databinding.ActivityManagerMainBinding
import com.example.managerapp.databinding.ActivityUpdateManagerBinding

class ManagerMainActivity : AppCompatActivity() {

    lateinit var binding: ActivityManagerMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        replaceFragment(ViewManagerFragment())



    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}