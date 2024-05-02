package com.example.managerapp.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.managerapp.R
import com.example.managerapp.databinding.ActivityAdminLoginBinding
import com.example.managerapp.databinding.ActivityLoginTeamLeaderBinding
import com.example.managerapp.utils.Prefs
import com.example.managerapp.view.fragment.teamleader.TeamLeaderMainActivity
import com.example.managerapp.viewModel.RegisterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminLoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityAdminLoginBinding
    lateinit var viewModel: RegisterViewModel
    val PERMISSION_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        viewModel = RegisterViewModel()
        binding.progreebar.visibility = View.GONE


        val leaderLogin = Prefs.adminIsLoggedIn()
        val leaderEmail = Prefs.getAdminEmail()
        if(leaderLogin){
            binding.progreebar.visibility = View.VISIBLE
            lifecycleScope.launch {
                val res = viewModel.checkIsLogin(leaderEmail,"4")
                if(res.toInt() > 0){
                    binding.progreebar.visibility = View.GONE
                    val intent =
                        Intent(this@AdminLoginActivity, SelectAsLoginActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this@AdminLoginActivity,"Login", Toast.LENGTH_LONG).show()
                    finish()
                    overridePendingTransition(R.anim.silde_in, R.anim.slide_out)
                }else{
                    binding.progreebar.visibility = View.GONE
                    Toast.makeText(this@AdminLoginActivity,"Please do login", Toast.LENGTH_LONG).show()
                }
            }

        }

        binding.submitButton.setOnClickListener {
            binding.progreebar.visibility = View.VISIBLE
            binding.submitButton.isEnabled = false
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            lifecycleScope.launch {

                val r = viewModel.loginAdmin(email, password)
                withContext(Dispatchers.Main) {
                    binding.submitButton.isEnabled = true
                    binding.progreebar.visibility = View.GONE

                    if (r?.toInt() != 0) {
                        Prefs.setAdminEmail(email)
                        Toast.makeText(
                            this@AdminLoginActivity,
                            "Login Successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent =
                            Intent(this@AdminLoginActivity, SelectAsLoginActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.silde_in, R.anim.slide_out)
                        finish()
                    } else {
                        Toast.makeText(
                            this@AdminLoginActivity,
                            "Credentials are wrong",
                            Toast.LENGTH_LONG
                        ).show()

                    }

                }
            }
        }

    }
}