package com.example.managerapp

import android.R
import android.R.attr.data
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.managerapp.adapter.CityCustomSpinnerAdapter
import com.example.managerapp.databinding.ActivityNewLeadBinding
import com.example.managerapp.model.CityModel
import com.example.managerapp.model.SignupModel
import com.example.managerapp.model.SpinnerDecoratorModel
import com.example.managerapp.network.ApiController
import com.example.managerapp.network.ApiInterface
import com.example.managerapp.repository.Repository
import com.example.managerapp.viewModel.RegisterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import java.util.Calendar
import kotlin.properties.Delegates


class NewLeadActivity : AppCompatActivity() {

    lateinit var binding: ActivityNewLeadBinding
    lateinit var registerViewModel: RegisterViewModel
    val SELECT_PICTURE = 200
    var timepick  = false
     var imagepath : MultipartBody.Part? = null
    var leadID= "1"
    val notificationTitle = "New Lead Arrived"
    val notificationBody = "checkout the new lead"

    val lists : List<CityModel>? = null
    var adapter = lists?.let { CityCustomSpinnerAdapter(this, it) }

    var cityIds = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewLeadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.spinner.visibility = View.VISIBLE
        binding.spinner.adapter = adapter

        adapter?.add(CityModel(0,"Choose City"))

        binding.headerLayout.headerTitle.text = "Generate New Lead"

        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        registerViewModel.fetchCityData()
        observeCityViewModel()

        binding.img.setOnClickListener {

            Intent(Intent.ACTION_PICK).also {
                it.type = "image/*"
                val mimeType = arrayOf("images/jpeg","images/png")
                it.putExtra(Intent.EXTRA_MIME_TYPES,mimeType)
                startActivityForResult(it,SELECT_PICTURE)
            }

        }


        binding.datepickbtn.setOnClickListener {

                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    this,
                    { view, year, monthOfYear, dayOfMonth ->
                        Toast.makeText(this,monthOfYear.toString(),Toast.LENGTH_LONG).show()

                        if(monthOfYear > 8){
                            binding.datepicktxt.text =
                                (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                        }else{
                            binding.datepicktxt.text =
                                (dayOfMonth.toString() + "-" + "0"+(monthOfYear + 1) + "-" + year)
                        }

                    },
                    year,
                    month,
                    day
                )
                datePickerDialog.show()
            }


        binding.timepickbtn.setOnClickListener {
            if(timepick  == false){
                timepick = true
                binding.timepickbtn.text = "Done"
                binding.timePicker1.visibility = View.VISIBLE
                binding.timepicktxt.visibility = View.GONE

                binding.timePicker1.setOnTimeChangedListener { _, hour, minute -> var hour = hour
                    var am_pm = ""
                    when {hour == 0 -> { hour += 12
                        am_pm = "AM"
                    }
                        hour == 12 -> am_pm = "PM"
                        hour > 12 -> { hour -= 12
                            am_pm = "PM"
                        }
                        else -> am_pm = "AM"
                    }
                    if (binding.timepicktxt != null) {
                        val hour = if (hour < 10) "0" + hour else hour
                        val min = if (minute < 10) "0" + minute else minute
                        // display format of time
                        val msg = "$hour : $min $am_pm"
                        binding.timepicktxt.visibility = View.VISIBLE
                        binding.timepicktxt .text = msg

                    }
                }


            }else{
                timepick = false
                binding.timepickbtn.text = "Pick Time"
                binding.timePicker1.visibility = View.GONE
                binding.timepicktxt.visibility = View.VISIBLE
            }
        }




        binding.submitButtonTlData.setOnClickListener {
            val cityiddata = cityIds.toString()
            var name = binding.editTextName.text.toString()
            var mobile = binding.editTextMobile.text.toString()
            var whatsapp = binding.editTextWhatsapp.text.toString()
            var decor_date = binding.datepicktxt.text.toString()
            var decor_time = binding.timepicktxt.text.toString()
            var description = binding.editTextDescription.text.toString()
            var total_amount = binding.editTextTotalAmount.text.toString()
            var recieve_amount = binding.editTextRecieveAmount.text.toString()
            var address = binding.editTextAddress.text.toString()
            var location = binding.editTextLocation.text.toString()

            if(name.equals("") || mobile.equals("") || whatsapp.equals("") || decor_date.equals("")
                || decor_time.equals("") || description.equals("") || total_amount.equals("") || recieve_amount.equals("")
                || address.equals("") || location.equals(""))
            {
                Toast.makeText(this,"Please check the fields",Toast.LENGTH_LONG).show()
            }else{
                if (cityIds>0){

                    var images  = imagepath.toString()

                    var forUpdateOrNot = "1"
                    var tlId = ""

                    lifecycleScope.launch {
                        val res = registerViewModel.insertLead(leadID,forUpdateOrNot,name,cityiddata,images,mobile,whatsapp,decor_date,decor_time,description,total_amount
                            ,recieve_amount,address,location)

                        if(res!!.body()!!.id.toInt() > 0){
                            withContext(Dispatchers.Main){
                                Toast.makeText(this@NewLeadActivity,res!!.body()!!.message,Toast.LENGTH_LONG).show()
                                tlId = res!!.body()!!.id
                            }

                            val response =  registerViewModel.sendNotification(tlId,notificationTitle,notificationBody)

                            withContext(Dispatchers.Main){
                                Toast.makeText(this@NewLeadActivity,response?.body()?.success.toString(),Toast.LENGTH_LONG).show()
                                delay(500)
                                val intent = Intent(this@NewLeadActivity,MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }else{
                            Toast.makeText(this@NewLeadActivity,res!!.body()!!.message,Toast.LENGTH_LONG).show()
                        }


                    }
                }else{
                    Toast.makeText(this,"PLease Select City",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun observeCityViewModel() {

        registerViewModel.cityData.observe(this@NewLeadActivity, Observer { items ->
            adapter = CityCustomSpinnerAdapter(this,items)
            binding.spinner.adapter = adapter
//            Toast.makeText(this,items.size,Toast.LENGTH_LONG).show()
        })

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                        val selectedItem = adapter?.getItem(position)
                        selectedItem?.let {
                            cityIds = it.id
                            Toast.makeText(this@NewLeadActivity,it.city_name,Toast.LENGTH_LONG).show()

                    }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected
            }
        }
    }


}