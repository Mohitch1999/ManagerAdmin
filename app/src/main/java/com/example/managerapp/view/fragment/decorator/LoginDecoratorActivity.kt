package com.example.managerapp.view.fragment.decorator

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.managerapp.MainActivity
import com.example.managerapp.R
import com.example.managerapp.databinding.ActivityLoginDecoratorBinding
import com.example.managerapp.utils.Prefs
import com.example.managerapp.view.fragment.teamleader.TeamLeaderMainActivity
import com.example.managerapp.viewModel.RegisterViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginDecoratorActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginDecoratorBinding
    lateinit var viewModel: RegisterViewModel
    val PERMISSION_CODE = 100
    var token = ""

    val LOCATION_PERMISSION_REQUEST_CODE = 1001
    val LOCATION_FINE_PERMISSION_REQUEST_CODE = 1002

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // globally declare LocationRequest
    private lateinit var locationRequest: LocationRequest

    // globally declare LocationCallback
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginDecoratorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = RegisterViewModel()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)

        // Permissions
        takePermission()
        requestLocationPermission()

        val decoratorId = Prefs.decoratorIsLoggedIn()
        val decoratorEmail = Prefs.getDecoratorEmail()

        if(decoratorId){
            binding.progressBar.visibility = View.VISIBLE

            lifecycleScope.launch {
                val res = viewModel.checkIsLogin(decoratorEmail,"3")
                if(res.toInt() > 0) {
                    binding.progressBar.visibility = View.GONE

                    val intent =
                        Intent(this@LoginDecoratorActivity, DecoratorMainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this@LoginDecoratorActivity,"Login",Toast.LENGTH_LONG).show()
                    finish()
                }else{
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@LoginDecoratorActivity,"No data",Toast.LENGTH_LONG).show()

                }
            }


        }

        viewModel = RegisterViewModel()
        binding.progressBar.visibility = View.GONE
        binding.signupTxt.text = "Login Decorator"



        binding.submitButton.setOnClickListener {
            generateToken()
            binding.progressBar.visibility = View.VISIBLE
            binding.submitButton.isEnabled = false

            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()




            lifecycleScope.launch {

                val r = viewModel.loginDecorator(email, password)
                withContext(Dispatchers.Main) {
                    binding.submitButton.isEnabled = true
                    binding.progressBar.visibility = View.GONE
                    if (r.toInt() != 0) {
                        Prefs.setDecoratorToken(token)
                        Prefs.setDecoratorEmail(email)
                        Prefs.setDecoratorId(r.toInt())
                        Toast.makeText(
                            this@LoginDecoratorActivity,
                            "Login Successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent =
                            Intent(this@LoginDecoratorActivity, DecoratorMainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@LoginDecoratorActivity,
                            "Credentials are wrong",
                            Toast.LENGTH_LONG
                        ).show()

                    }

                }
            }
        }


    }

    fun  takePermission() {
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

    fun   generateToken() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("token", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            token = task.result

            // Log and toast
//            val msg = getString(R.string.msg_token_fmt, token)
            Log.d("token", "token generated = " + token)
//            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    generateToken()
                    requestLocationPermission()
                    for (i in grantResults){
                        Log.d("grant",i.toString())
                    }
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, get the location
                    getCurrentLocation()
                    if(requestCode == LOCATION_FINE_PERMISSION_REQUEST_CODE){
                        if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                            getCurrentLocation()
                        }
                    }else{
                        requestFineLocationPermission()
                    }

                } else {
                    // Permission denied, handle it or inform the user
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(){
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                val latitude =  location?.latitude
                val longitude = location?.longitude
                Prefs.setLatitude(latitude.toString())
                Prefs.setLongitude(longitude.toString())
//                Toast.makeText(this@LoginDecoratorActivity,"Location id $latitude and $longitude",Toast.LENGTH_LONG).show()
//                Log.d("locations","$latitude $longitude is the location")

            }
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission is already granted, get the location
            getCurrentLocation()
           requestFineLocationPermission()
        }
    }
    private fun requestFineLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_FINE_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission is already granted, get the location
            getCurrentLocation()
        }
    }
}