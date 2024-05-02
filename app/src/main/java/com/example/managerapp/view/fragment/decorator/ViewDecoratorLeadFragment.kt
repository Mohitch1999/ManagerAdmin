package com.example.managerapp.view.fragment.decorator

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
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
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.managerapp.R
import com.example.managerapp.adapter.MyAdapter
import com.example.managerapp.model.LeadModel
import com.example.managerapp.utils.Prefs
import com.example.managerapp.utils.Utility
import com.example.managerapp.view.activity.SelectAsLoginActivity
import com.example.managerapp.view.fragment.notification.NotificationFragment
import com.example.managerapp.view.fragment.teamleader.ViewLeadDetailFragment
import com.example.managerapp.viewModel.RegisterViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.iceteck.silicompressorr.SiliCompressor
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions
import java.util.Calendar


class ViewDecoratorLeadFragment : Fragment() {

    lateinit var viewModel: RegisterViewModel
    private var leadListmodel : List<LeadModel>? = null
    val newadpter = MyAdapter()
    lateinit var progressbar :ProgressBar
    lateinit var recyclerView : RecyclerView
    lateinit var nodata : TextView
    var decoratorId = ""
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private val LOCATION_FINE_PERMISSION_REQUEST_CODE = 1002

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // globally declare LocationRequest
    private lateinit var locationRequest: LocationRequest

    // globally declare LocationCallback
    private lateinit var locationCallback: LocationCallback

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_decorator_lead, container, false)

        recyclerView = view.findViewById<RecyclerView>(R.id.leadRecycler)
        val viewtlbtn = view.findViewById<MaterialButton>(R.id.view_tl_btn)
        val addtlbtn = view.findViewById<Button>(R.id.add_tl_btn)
        val addleadbtn = view.findViewById<Button>(R.id.add_lead_btn)
        val allleadbtn = view.findViewById<Button>(R.id.view_all_tl_btn)
        val pendingleadbtn = view.findViewById<Button>(R.id.view_pending_btn)
        val completeleadbtn = view.findViewById<Button>(R.id.view_complete_btn)
        val viewDateBtn = view.findViewById<MaterialButton>(R.id.view_date_btn)
        val headerTitle = view.findViewById<TextView>(R.id.header_title)
        val search = view.findViewById<FloatingActionButton>(R.id.search)
        val headerNotificationBtn = view.findViewById<ImageButton>(R.id.header_btn)
        headerNotificationBtn.visibility = View.VISIBLE
        val headerLogoutBtn = view.findViewById<ImageButton>(R.id.header_btn_logout)
        headerLogoutBtn.visibility = View.VISIBLE

        headerTitle.text = "View Leads"

        progressbar = view.findViewById<ProgressBar>(R.id.progressbar)
        nodata = view.findViewById<TextView>(R.id.nodata)

        progressbar.visibility = View.VISIBLE

        val id = Prefs.getDecoratorId().toString()
        val token = Prefs.getDecoratorToken().toString()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        requestLocationPermission()

        search.setOnClickListener {
            showBottomDialog()
        }

        headerLogoutBtn.setOnClickListener {
            showAlertDialogForLogout()
        }

        headerNotificationBtn.setOnClickListener {

            val bundle = Bundle().apply {
                putString("id", id)
                putString("choose_id","3")
            }
            Utility.changeFragmentWithNotificationData(NotificationFragment(),requireActivity(),bundle)

        }

        val recycler = recyclerView
        recycler.adapter = newadpter
        recycler.layoutManager = LinearLayoutManager(requireContext())
        decoratorId = Prefs.getDecoratorId().toString()

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        callLeadApi()

        lifecycleScope.launch {
            val tokenstatus = viewModel.updateTokenDecorator(id,token)
            Log.d("token status",tokenstatus)
        }

        newadpter.setOnItemClickListener {

            val bundle = Bundle().apply {
                putString("name", it.name)
                putString("mobile", it.mobile)
                putString("id",it.id)
                putString("whatsapp",it.whatsapp)
                putString("total",it.total_amount)
                putString("recieve",it.recieve_amount)
                putString("desc",it.description)
                putString("location",it.location)
                putString("date",it.decor_date)
                putString("register_date",it.register_date)
                putString("time",it.decor_time)
                putString("address",it.address)
                putString("tlId",it.tid)
                putString("status",it.status)
                putString("check_tl_or_decor","3")
            }

            Utility.changeFragmentWithData(ViewLeadDetailFragment(),requireActivity(),bundle)

        }

        newadpter.setOnItemClickListenerDelete {
            showAlertDialog(it.id)
        }

        return view
    }


    override fun onResume() {
        super.onResume()
        viewModel.fetchLeadData()
    }


    private fun showAlertDialog(id :String) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.apply {
            setTitle("Delete Lead")
            setMessage("Are you sure you want to delete?")
            setPositiveButton("Yes") { dialog, which ->
                lifecycleScope.launch {
                    progressbar.visibility = View.VISIBLE
                    val res = viewModel.deleteLeadById(id,"4")
                    if(res?.body()?.id?.toInt() == 1){
                        showToast(res?.body()!!.message)
                        callLeadApi()
                    }else{
                        showToast("something wrong")
                    }
                }

            }
            setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }

            create().show()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun callLeadApi(){

        lifecycleScope.launch {
            progressbar.visibility = View.VISIBLE
            val leads = viewModel.getDecoratorLeadById(decoratorId)
            if (leads?.size!! > 0){
                newadpter.setData(leads,requireContext())
                leadListmodel = leads
                nodata.visibility = View.GONE
            }else{
                recyclerView.visibility = View.GONE
                nodata.text = "Empty Leads Box"
                nodata.visibility = View.VISIBLE
            }
            progressbar.visibility = View.GONE
        }
    }

    private fun showAlertDialogForLogout() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.apply {
            setTitle("Logout")
            setMessage("Are you sure you want to logout?")
            setPositiveButton("Yes") { dialog, which ->

                Prefs.clearall(requireContext())
                val intent = Intent(requireContext(), SelectAsLoginActivity::class.java)
                startActivity(intent)
                activity?.finish()

            }
            setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }

            create().show()
        }
    }


    fun openMap(latitude: Double, longitude: Double) {
        val geoUri = "geo:$latitude,$longitude"
        val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
        mapIntent.setPackage("com.google.android.apps.maps") // Specify the package name to open in Google Maps app
        if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(mapIntent)
        } else {
            // If Google Maps app is not installed, open in a web browser
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com/?q=$latitude,$longitude"))
            if (webIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(webIntent)
            } else {
                // No application available to handle maps
                // Handle the situation appropriately
                Toast.makeText(requireContext(), "No application available to handle maps", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
           obtieneLocalizacion()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get the location
                obtieneLocalizacion()
                if(requestCode == LOCATION_FINE_PERMISSION_REQUEST_CODE){
                    if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        obtieneLocalizacion()
                    }
                }else{
                    requestLocationPermission()
                }

            } else {
                // Permission denied, handle it or inform the user
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }



//    }

    //start location updates
    @SuppressLint("MissingPermission")
    private fun obtieneLocalizacion(){
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                val latitude =  location?.latitude
                val longitude = location?.longitude
//                Toast.makeText(requireContext(),"Location id $latitude and $longitude",Toast.LENGTH_LONG).show()
                Log.d("locations","$latitude $longitude is the location")

            }
    }

    private fun showBottomDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)

        // Example of interacting with views in the bottom dialog
        val viewAllBtn = bottomSheetView.findViewById<MaterialButton>(R.id.view_all_tl_btn)
        val viewPendingBtn = bottomSheetView.findViewById<MaterialButton>(R.id.view_pending_btn)
        val viewCompleteBtn = bottomSheetView.findViewById<MaterialButton>(R.id.view_complete_btn)
        val viewDateBtn = bottomSheetView.findViewById<MaterialButton>(R.id.view_date_btn)

        viewAllBtn.setOnClickListener {
            val filterdata =   leadListmodel?.filter {
                it.mobile.toInt() > 0
            }
            newadpter.setData(filterdata,requireContext())
        }

        viewPendingBtn.setOnClickListener {
            val filterdata =   leadListmodel?.filter {
                it.status.toInt() < 5
            }
            newadpter.setData(filterdata,requireContext())
        }

        viewCompleteBtn.setOnClickListener {
            val filterdata =   leadListmodel?.filter {
                it.status.toInt() == 5
            }
            newadpter.setData(filterdata,requireContext())
        }

        viewDateBtn.setOnClickListener {
            var currentDate = ""
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->

                    if(monthOfYear > 8){
                        currentDate =
                            (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                        val filterdata =   leadListmodel?.filter {
                            it.decor_date == currentDate
                        }
                        newadpter.setData(filterdata,requireContext())
                    }else{
                        currentDate =
                            (dayOfMonth.toString() + "-" + "0"+(monthOfYear + 1) + "-" + year)
                        val filterdata =   leadListmodel?.filter {
                            it.decor_date == currentDate
                        }
                        newadpter.setData(filterdata,requireContext())
                    }

                },
                year,
                month,
                day
            )
            datePickerDialog.show()

        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

}