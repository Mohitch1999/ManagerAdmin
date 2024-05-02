package com.example.managerapp.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.managerapp.R
import com.example.managerapp.databinding.ActivityHrAttendanceBinding
import com.example.managerapp.view.fragment.attendance.AttendanceFragment
import com.example.managerapp.view.fragment.attendance.DecoratorsAttendanceFragment
import com.example.managerapp.view.fragment.attendance.TeamLeadersAttendanceFragment

class HrAttendanceActivity : AppCompatActivity() {

    lateinit var binding : ActivityHrAttendanceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityHrAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var chooseId = intent.getIntExtra("choose_id",0)

        if(chooseId == 1){
            replaceFragment(AttendanceFragment())
        }else if(chooseId == 2){
            replaceFragment(TeamLeadersAttendanceFragment())
        }else if(chooseId == 3){
            replaceFragment(DecoratorsAttendanceFragment())
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}