package com.example.managerapp.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.managerapp.R
import com.example.managerapp.databinding.ActivitySelectAsLoginBinding
import com.example.managerapp.view.fragment.decorator.LoginDecoratorActivity
import com.example.managerapp.view.fragment.manager.LoginManagerActivity
import com.example.managerapp.view.fragment.teamleader.LoginTeamLeaderActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch


class SelectAsLoginActivity : AppCompatActivity() {

    lateinit var binding: ActivitySelectAsLoginBinding

    val LOCATION_PERMISSION_REQUEST_CODE = 1001
    val LOCATION_FINE_PERMISSION_REQUEST_CODE = 1002
    val LOCATION_SYSTEM_REQUEST_CODE = 100

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivitySelectAsLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)

        requestLocationPermission()
        binding.loginLayout.headerTitle.text = "Choose, who you are?"
        binding.loginLayout.headerBtn.visibility = View.GONE;




        binding.managerLoginbtn.setOnClickListener {
            val intent = Intent(this@SelectAsLoginActivity,LoginManagerActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.silde_in,R.anim.slide_out)
        }

        binding.tLLoginbtn.setOnClickListener {
            val intent = Intent(this@SelectAsLoginActivity, LoginTeamLeaderActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.silde_in,R.anim.slide_out)
        }

        binding.technicianLoginbtn.setOnClickListener {
            val intent = Intent(this@SelectAsLoginActivity,LoginDecoratorActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.silde_in,R.anim.slide_out)
        }

        binding.hrLoginbtn.setOnClickListener {
            val intent = Intent(this@SelectAsLoginActivity,HRActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.silde_in,R.anim.slide_out)
        }

        var new_Token = ""
        new_Token = FirebaseMessaging.getInstance().token.toString()
        Log.d("FirebaseToken", "" + new_Token)
//        lifecycleScope.launch(Dispatchers.IO){
//            new_Token = FirebaseMessaging.getInstance().token.await()
//            Log.d("FirebaseToken", "" + new_Token)
//        }



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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation()
//                    requestFineLocationPermission()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            LOCATION_FINE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, get the location
                    getCurrentLocation()

                } else {
                    // Permission denied, handle it or inform the user
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    // Handle the result of the location settings activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_SYSTEM_REQUEST_CODE) {
            // Check if location services are now enabled
            if (isLocationEnabled()) {
                // Location services are enabled, handle it appropriately
                // For example, start location updates or perform location-based actions
                requestLocationPermission()
            } else {
                // Location services are still not enabled, handle it or inform the user
                finish()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(){
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                val latitude =  location?.latitude
                val longitude = location?.longitude
//                Prefs.setLatitude(latitude.toString())
//                Prefs.setLongitude(longitude.toString())
//                Toast.makeText(this@SelectAsLoginActivity,"Location id $latitude and $longitude",Toast.LENGTH_LONG).show()
                Log.d("locations","$latitude $longitude is the location")

            }
    }



    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    // Function to prompt the user to enable location services
    private fun requestLocationServices() {
        val locationSettingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivityForResult(locationSettingsIntent, LOCATION_SYSTEM_REQUEST_CODE)
    }

    override fun onStart() {
        super.onStart()
        // Check if location services are enabled when the activity resumes
        if (!isLocationEnabled()) {

            // Location services are not enabled, request the user to enable them
//            requestLocationServices()
            showAlertDialog()
        }

    }

    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.apply {
            setTitle("Location")
            setMessage("Please enable location before proceed further?")
            setPositiveButton("Yes") { dialog, which ->
                requestLocationServices()
            }
            setNegativeButton("No") { dialog, which ->
                finish()
            }

            create().show()
        }
    }

}