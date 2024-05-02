package com.example.managerapp.view.fragment.decorator

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.managerapp.R
import com.example.managerapp.model.LeadModel
import com.example.managerapp.utils.Prefs
import com.example.managerapp.utils.Utility
import com.example.managerapp.view.fragment.manager.CashImageFragment
import com.example.managerapp.view.fragment.manager.ViewLeadImagesFragment
import com.example.managerapp.view.fragment.manager.ViewLeadVideoFragment
import com.example.managerapp.viewModel.RegisterViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pub.devrel.easypermissions.EasyPermissions
import java.util.Locale


class UpdateLeadStatusFragment : Fragment() {

    lateinit var leaveForDecorationBtn : Button
    lateinit var reachedDecorationBtn : Button
    lateinit var videoBeforeDecorationBtn : Button
    lateinit var videoAfterDecorationBtn : Button
    lateinit var cashOrOnlineBtn : Button
    lateinit var viewLeadImage : Button
    lateinit var viewCashImage : Button
    lateinit var viewVideo : Button
    lateinit var progressbar : ProgressBar
    lateinit var title : TextView
    lateinit var collectCash : TextView
    lateinit var viewModel: RegisterViewModel
     var leadModel: List<LeadModel>? = null
    var id = ""
    var decorator_id = ""
    var leadStatus = 0
    var leadname = ""
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
     var latitude  = 0.0
     var longitude = 0.0


    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_update_lead_status, container, false)

            leaveForDecorationBtn = view.findViewById(R.id.leave_for_decoration_btn)
            reachedDecorationBtn = view.findViewById(R.id.reached_decoration_btn)
            videoBeforeDecorationBtn = view.findViewById(R.id.video_before_decoration_btn)
            videoAfterDecorationBtn = view.findViewById(R.id.video_after_decoration_btn)
            cashOrOnlineBtn = view.findViewById(R.id.cash_or_online_btn)
            title = view.findViewById(R.id.lead_title)
            progressbar = view.findViewById(R.id.progressBar)
            viewLeadImage = view.findViewById(R.id.view_lead_image_btn)
            viewCashImage = view.findViewById(R.id.view_cash_image_btn)
            viewVideo = view.findViewById(R.id.view_video_btn)
        collectCash = view.findViewById(R.id.collect_cash)
        val headerTitle = view.findViewById<TextView>(R.id.header_title)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

//        requestLocationPermission()
        headerTitle.text = "Update Lead Status"

        progressbar.visibility = View.GONE

         id = requireArguments().getInt("id").toString()
        decorator_id = Prefs.getDecoratorId().toString()
        leadname = requireArguments().getString("name").toString()
        val total_amount = requireArguments().getString("total_amount")?.toInt()
        val recieve_amount = requireArguments()?.getString("recieve_amount")?.toInt()
        val status = requireArguments().getString("status")



        collectCash.visibility = View.GONE
        var remain_amount= total_amount!! - recieve_amount!!

        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                } else -> {
                // No location access granted.
            }
            }
        }


        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // reuqest for permission
            requestLocationPermission()
        } else {
            // already permission granted
            // get location here
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    // Got last known location. In some rare situations this can be null.
                    if(location!=null){
                        latitude = location.latitude
                        longitude = location.longitude
                    }
                }
        }



        if(status?.toInt() == 4){
            viewCashImage.visibility = View.GONE
            viewVideo.visibility = View.VISIBLE
            collectCash.visibility = View.VISIBLE
            collectCash.text = "Collect Cash : $remain_amount"
        }else if(status?.toInt() == 5){
            viewCashImage.visibility = View.VISIBLE
            viewVideo.visibility = View.VISIBLE
        }else{
            viewCashImage.visibility = View.GONE
            viewVideo.visibility = View.GONE
        }
        title.text = leadname
//        Toast.makeText(requireContext(), "$name $id"
//        ,Toast.LENGTH_LONG).show()

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        lifecycleScope.launch {
            progressbar.visibility = View.VISIBLE

            leaveForDecorationBtn.isEnabled = false
            reachedDecorationBtn.isEnabled = false
            videoBeforeDecorationBtn.isEnabled = false
            videoAfterDecorationBtn.isEnabled = false
            cashOrOnlineBtn.isEnabled = false


          var res =  viewModel.getDecoratorLeadStatusById(id!!)
            if (res != null) {
                leadModel = res
                withContext(Dispatchers.Main){

                    if (leadModel?.get(0)?.status == "1"){
                        leaveForDecorationBtn.visibility = View.GONE
                        reachedDecorationBtn.visibility = View.VISIBLE
                        videoBeforeDecorationBtn.visibility = View.GONE
                        videoAfterDecorationBtn.visibility = View.GONE
                        cashOrOnlineBtn.visibility = View.GONE
                    }else if(leadModel?.get(0)?.status == "2"){
                        leaveForDecorationBtn.visibility = View.GONE
                        reachedDecorationBtn.visibility = View.GONE
                        videoBeforeDecorationBtn.visibility = View.VISIBLE
                        videoAfterDecorationBtn.visibility = View.GONE
                        cashOrOnlineBtn.visibility = View.GONE
                    }else if(leadModel?.get(0)?.status == "3"){
                        leaveForDecorationBtn.visibility = View.GONE
                        reachedDecorationBtn.visibility = View.GONE
                        videoBeforeDecorationBtn.visibility = View.GONE
                        videoAfterDecorationBtn.visibility = View.VISIBLE
                        cashOrOnlineBtn.visibility = View.GONE
                    }else if(leadModel?.get(0)?.status == "4"){
                        leaveForDecorationBtn.visibility = View.GONE
                        reachedDecorationBtn.visibility = View.GONE
                        videoBeforeDecorationBtn.visibility = View.GONE
                        videoAfterDecorationBtn.visibility = View.GONE
                        cashOrOnlineBtn.visibility = View.VISIBLE

                        viewVideo.visibility = View.VISIBLE
                        collectCash.visibility = View.VISIBLE
                        collectCash.text = "Collect Cash : $remain_amount rs"
                    }
                    else if(leadModel?.get(0)?.status == "5"){
                        leaveForDecorationBtn.visibility = View.GONE
                        reachedDecorationBtn.visibility = View.GONE
                        videoBeforeDecorationBtn.visibility = View.GONE
                        videoAfterDecorationBtn.visibility = View.GONE
                        cashOrOnlineBtn.visibility = View.GONE
                        collectCash.visibility = View.GONE
                    }
                    progressbar.visibility = View.GONE

                    leaveForDecorationBtn.isEnabled = true
                    reachedDecorationBtn.isEnabled = true
                    videoBeforeDecorationBtn.isEnabled = true
                    videoAfterDecorationBtn.isEnabled = true
                    cashOrOnlineBtn.isEnabled = true

                }

            }
        }

        leaveForDecorationBtn.setOnClickListener {
            progressbar.visibility = View.VISIBLE
            leadStatus = 1
            lifecycleScope.launch {
              val res =  viewModel.updateDecoratorLeadStatus(id!!,"1")
                if(res)
                  sendNotification("Lead Status","Decorator Leave for Decoration")
            }
        }

        reachedDecorationBtn.setOnClickListener {
            leadStatus = 2
            progressbar.visibility = View.VISIBLE

            lifecycleScope.launch {
               val res =  viewModel.updateDecoratorLeadStatus(id!!,"2")
                if(res)
                    sendNotification("Lead Status","Decorator reached at location")
            }
        }

        videoBeforeDecorationBtn.setOnClickListener {
            lifecycleScope.launch {
                val intent = Intent(requireContext(),UploadVideoActivity::class.java)
                intent.putExtra("id",id)
                intent.putExtra("leadname",leadname)
                intent.putExtra("btn","1")
                startActivity(intent)
                activity?.overridePendingTransition(R.anim.silde_in, R.anim.slide_out)

            }

        }

        videoAfterDecorationBtn.setOnClickListener {
            lifecycleScope.launch {
                val intent = Intent(requireContext(),UploadVideoActivity::class.java)
                intent.putExtra("id",id)
                intent.putExtra("leadname",leadname)
                intent.putExtra("btn","2")
                startActivity(intent)
                activity?.overridePendingTransition(R.anim.silde_in, R.anim.slide_out)

            }

        }


        viewLeadImage.setOnClickListener {
            val bundle = Bundle().apply {
                putString("id", id)
                putString("verify", "3")
            }

            Utility.changeFragmentWithData(ViewLeadImagesFragment(),requireActivity(),bundle)
        }

        viewCashImage.setOnClickListener {
            val bundle = Bundle().apply {
                putString("id", id)
            }

            Utility.changeFragmentWithData(CashImageFragment(),requireActivity(),bundle)
        }

        viewVideo.setOnClickListener {
            val bundle = Bundle().apply {
                putString("id", id)
            }

            Utility.changeFragmentWithData(ViewLeadVideoFragment(),requireActivity(),bundle)
        }
        cashOrOnlineBtn.setOnClickListener {
            val intent = Intent(requireContext(),CashImageActivity::class.java)
            intent.putExtra("id",id)
            intent.putExtra("leadname",leadname)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.silde_in, R.anim.slide_out)
        }
        return view
    }

    @SuppressLint("SuspiciousIndentation")
    fun sendNotification(title : String, body : String){
        lifecycleScope.launch {
          val res =   viewModel.sendMultipleFirebaseApiDecorator(id,title, body)
            if(res.toInt() == 1){
                    progressbar.visibility = View.GONE
                    getFragmentManager()?.popBackStack()
            }
        }

        sendLocation(id,decorator_id,latitude.toString(),longitude.toString(),leadStatus)
    }

    fun sendLocation(id : String,decoratorId : String,lat : String ,long :String,leadStatus : Int){
        lifecycleScope.launch(Dispatchers.IO ) {
            var response = viewModel.createDecoratorLocation(id,decorator_id,lat,long,leadname,leadStatus)
            if(response.toInt()==1){
                withContext(Dispatchers.Main){
                    Toast.makeText(requireContext(),"location update",Toast.LENGTH_LONG).show()
                    Log.d("locations",response + " Location update")
                }

            }
        }
    }


    private fun requestLocationPermission() {
        if (EasyPermissions.hasPermissions(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            && EasyPermissions.hasPermissions(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // Permission already granted, proceed to get location
            getLocation()
        } else {
            // Request location permission
            EasyPermissions.requestPermissions(
                this,
                "This app needs access to your location to provide accurate information.",
                LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, requireContext())
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations, this can be null.
                if (location != null) {
                    // Use the location
                     latitude = location.latitude
                     longitude = location.longitude

                    Toast.makeText(requireContext(), "get location: $latitude $longitude", Toast.LENGTH_SHORT).show()
//                    openMap(latitude, longitude)
                }else{
                    requestLocationUpdates()
                }
            }
            .addOnFailureListener { e ->
                // Failed to get location
                Toast.makeText(requireContext(), "Failed to get location: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000 // Update location every 10 seconds
            fastestInterval = 10000 // Update location at least every 5 seconds
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                p0 ?: return
                for (location in p0.locations) {
                    // Use the location
                    latitude = location.latitude
                    longitude = location.longitude
                    Toast.makeText(requireContext(), "get location: $latitude $longitude", Toast.LENGTH_SHORT).show()

                    // Do something with latitude and longitude
                }
            }
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }


}