package com.example.managerapp.view.fragment.teamleader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.managerapp.R
import com.example.managerapp.databinding.ActivityTeamLeaderMainBinding
class TeamLeaderMainActivity : AppCompatActivity() {

    lateinit var binding: ActivityTeamLeaderMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamLeaderMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        replaceFragment(TeamLeaderViewLeadFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}