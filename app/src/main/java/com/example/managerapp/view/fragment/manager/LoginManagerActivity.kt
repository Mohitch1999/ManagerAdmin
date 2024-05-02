package com.example.managerapp.view.fragment.manager

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.managerapp.MainActivity
import com.example.managerapp.R
import com.example.managerapp.databinding.ActivityLoginManagerBinding
import com.example.managerapp.utils.Prefs
import com.example.managerapp.view.fragment.teamleader.TeamLeaderMainActivity
import com.example.managerapp.viewModel.RegisterViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginManagerActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginManagerBinding
    lateinit var viewModel: RegisterViewModel
    val PERMISSION_CODE = 100
    var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        takePermission()

        viewModel = RegisterViewModel()
        binding.progreebar.visibility = View.GONE
        binding.signupTxt.text = "Login Manager"


        val managerLogin = Prefs.managerIsLoggedIn()
        val managerEmail = Prefs.getManagerEmail()
        if(managerLogin){
            binding.progreebar.visibility = View.VISIBLE
           lifecycleScope.launch {

               val res = viewModel.checkIsLogin(managerEmail,"1")
               if(res.toInt() > 0) {
//                   progress.dismiss()
                   binding.progreebar.visibility = View.GONE

                   val intent =
                       Intent(this@LoginManagerActivity, MainActivity::class.java)
                   startActivity(intent)
                   Toast.makeText(this@LoginManagerActivity,"Login",Toast.LENGTH_LONG).show()
                   finish()
               }else{
//                   progress.dismiss()
                   binding.progreebar.visibility = View.GONE

                   Toast.makeText(this@LoginManagerActivity,"No data",Toast.LENGTH_LONG).show()

               }

           }

        }

        binding.submitButton.setOnClickListener {
            generateToken()
            binding.progreebar.visibility = View.VISIBLE
            binding.submitButton.isEnabled = false

            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()



            lifecycleScope.launch {

                val r = viewModel.loginManager(email, password)
                withContext(Dispatchers.Main) {
                    binding.submitButton.isEnabled = true
                    binding.progreebar.visibility = View.GONE

                    if (r.toInt() != 0) {
                        Prefs.setMangerToken(token)
                        Prefs.setManagerEmail(email)
                        Prefs.setManagerId(r.toInt())
                        Toast.makeText(
                            this@LoginManagerActivity,
                            "Login Successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent =
                            Intent(this@LoginManagerActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@LoginManagerActivity,
                            "Credentials are wrong",
                            Toast.LENGTH_LONG
                        ).show()

                    }

                }
            }
        }

        binding.viewCredentials.setOnClickListener {
            val intent = Intent(this@LoginManagerActivity,ManagerMainActivity::class.java)
            startActivity(intent)
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


    @SuppressLint("MissingInflatedId")
    fun showLoadingDialog(context: Context): AlertDialog {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null)
//        val progressBar = dialogView.findViewById<ProgressBar>(R.id.progressBar)

        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)

        val dialog = builder.create()

        return dialog
    }

}