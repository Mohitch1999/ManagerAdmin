package com.example.managerapp.view.fragment.teamleader

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.managerapp.R
import com.example.managerapp.databinding.ActivityLoginTeamLeaderBinding
import com.example.managerapp.utils.Prefs
import com.example.managerapp.viewModel.RegisterViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginTeamLeaderActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginTeamLeaderBinding
    lateinit var viewModel: RegisterViewModel
    val PERMISSION_CODE = 100
    var token = ""

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginTeamLeaderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        viewModel = RegisterViewModel()
        binding.progreebar.visibility = View.GONE
        takePermission()


        val leaderLogin = Prefs.teamLeaderIsLoggedIn()
        val leaderEmail = Prefs.getTeamLeaderEmail()
        if(leaderLogin){
            binding.progreebar.visibility = View.VISIBLE
            lifecycleScope.launch {
                val res = viewModel.checkIsLogin(leaderEmail,"2")
                if(res.toInt() > 0){
                    binding.progreebar.visibility = View.GONE
                    val intent =
                        Intent(this@LoginTeamLeaderActivity, TeamLeaderMainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this@LoginTeamLeaderActivity,"Login",Toast.LENGTH_LONG).show()
                    finish()
                    overridePendingTransition(R.anim.silde_in, R.anim.slide_out)
                }else{
                    binding.progreebar.visibility = View.GONE
                    Toast.makeText(this@LoginTeamLeaderActivity,"Not Available",Toast.LENGTH_LONG).show()
                }
            }


        }

        binding.submitButton.setOnClickListener {
            binding.progreebar.visibility = View.VISIBLE
            binding.submitButton.isEnabled = false
            generateToken()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()




            lifecycleScope.launch {

                val r = viewModel.loginTeamLeader(email, password)
                withContext(Dispatchers.Main) {
                    binding.submitButton.isEnabled = true
                    binding.progreebar.visibility = View.GONE

                    if (r?.toInt() != 0) {
                        val id = r!!.toInt()
                        Prefs.setTeamLeaderId(id)
                        Prefs.setTeamLeaderEmail(email)
                        Prefs.setTeamLeaderToken(token)
                        Toast.makeText(
                            this@LoginTeamLeaderActivity,
                            "Login Successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent =
                            Intent(this@LoginTeamLeaderActivity, TeamLeaderMainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@LoginTeamLeaderActivity,
                            "Credentials are wrong",
                            Toast.LENGTH_LONG
                        ).show()

                    }

                }
            }
        }

    }

    fun takePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS
                )
                requestPermissions(permissions, PERMISSION_CODE)
            }
        }
    }

    fun generateToken() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("token", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            token = task.result

            // Log and toast
//            val msg = getString(R.string.msg_token_fmt, token)
            Log.d("token","token generated = " + token)
//            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    generateToken()
                }else{
                    Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}