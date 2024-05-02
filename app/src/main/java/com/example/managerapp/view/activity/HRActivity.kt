package com.example.managerapp.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.managerapp.R
import com.example.managerapp.databinding.ActivityHractivityBinding
import com.example.managerapp.databinding.ActivitySelectAsLoginBinding
import com.example.managerapp.view.fragment.decorator.LoginDecoratorActivity
import com.example.managerapp.view.fragment.manager.LoginManagerActivity
import com.example.managerapp.view.fragment.teamleader.LoginTeamLeaderActivity

class HRActivity : AppCompatActivity() {

    lateinit var binding: ActivityHractivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityHractivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginLayout.headerTitle.text = "Choose, which one wants to get?"
        binding.loginLayout.headerBtn.visibility = View.GONE;

        binding.managerLoginbtn.setOnClickListener {
            val intent = Intent(this@HRActivity, HrAttendanceActivity::class.java)
            intent.putExtra("choose_id",1)
            startActivity(intent)
            overridePendingTransition(R.anim.silde_in,R.anim.slide_out)
        }

        binding.tLLoginbtn.setOnClickListener {
            val intent = Intent(this@HRActivity, HrAttendanceActivity::class.java)
            intent.putExtra("choose_id",2)
            startActivity(intent)
            overridePendingTransition(R.anim.silde_in,R.anim.slide_out)
        }

        binding.technicianLoginbtn.setOnClickListener {
            val intent = Intent(this@HRActivity, HrAttendanceActivity::class.java)
            intent.putExtra("choose_id",3)
            startActivity(intent)
            overridePendingTransition(R.anim.silde_in,R.anim.slide_out)
        }

    }
}