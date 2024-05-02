package com.example.managerapp.view.fragment.decorator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.managerapp.R
import com.example.managerapp.databinding.ActivityDecoratorMainBinding
import com.example.managerapp.view.fragment.teamleader.TeamLeaderViewLeadFragment

class DecoratorMainActivity : AppCompatActivity() {

    lateinit var binding : ActivityDecoratorMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDecoratorMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        replaceFragment(ViewDecoratorLeadFragment())

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

}