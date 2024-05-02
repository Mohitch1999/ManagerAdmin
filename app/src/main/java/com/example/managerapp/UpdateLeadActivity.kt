package com.example.managerapp

import android.R
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.managerapp.adapter.CityCustomSpinnerAdapter
import com.example.managerapp.databinding.ActivityNewLeadBinding
import com.example.managerapp.databinding.ActivityUpdateLeadBinding
import com.example.managerapp.model.CityModel
import com.example.managerapp.viewModel.RegisterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import java.util.Calendar
import kotlin.properties.Delegates

class UpdateLeadActivity : AppCompatActivity() {

    lateinit var binding : ActivityUpdateLeadBinding

    lateinit var registerViewModel: RegisterViewModel
    val SELECT_PICTURE = 200
    var timepick  = false
    var imagepath : MultipartBody.Part? = null
    private val READ_EXTERNAL_STORAGE_PERMISSION_CODE = 101
    var check_is_lead_for_udate = ""
    var leadID= ""

    val lists : List<CityModel>? = null
    var adapter = lists?.let { CityCustomSpinnerAdapter(this, it) }
    var cityIds by Delegates.notNull<Int>()

    val notificationTitle = "Lead Updated"
    val notificationBody = "checkout the update lead"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateLeadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.headerLayout.headerTitle.text = "Update Lead"

        binding.spinner.visibility = View.VISIBLE
        binding.spinner.adapter = adapter
        leadID = intent.getStringExtra("id").toString()
        check_is_lead_for_udate = intent.getStringExtra("udpatestatus").toString()

        val name = intent.getStringExtra("name")
        val mobile = intent.getStringExtra("mobile")
        val whatsapp = intent.getStringExtra("whatsapp")
        val total = intent.getStringExtra("total")
        val recieve = intent.getStringExtra("recieve")
        val desc = intent.getStringExtra("desc")
        val location = intent.getStringExtra("location")
        val date = intent.getStringExtra("date")
        val time = intent.getStringExtra("time")
        val address = intent.getStringExtra("address")
        val tlId = intent.getStringExtra("tlId")

        binding.editTextName.text = Editable.Factory.getInstance().newEditable(name)
        binding.editTextMobile.text = Editable.Factory.getInstance().newEditable(mobile)
        binding.editTextWhatsapp.text = Editable.Factory.getInstance().newEditable(whatsapp)
        binding.datepicktxt.text = Editable.Factory.getInstance().newEditable(date)
        binding.timepicktxt.text = Editable.Factory.getInstance().newEditable(time)
        binding.editTextDescription.text = Editable.Factory.getInstance().newEditable(desc)
        binding.editTextTotalAmount.text = Editable.Factory.getInstance().newEditable(total)
        binding.editTextRecieveAmount.text = Editable.Factory.getInstance().newEditable(recieve)
        binding.editTextAddress.text = Editable.Factory.getInstance().newEditable(address)
        binding.editTextLocation.text = Editable.Factory.getInstance().newEditable(location)
        cityIds = tlId?.toInt()!!

        binding.timepicktxt.visibility = View.VISIBLE

        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        registerViewModel.fetchCityData()
        observeCityViewModel()

        binding.datepickbtn.setOnClickListener {

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->

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
                binding.timepicktxt.visibility = View.INVISIBLE

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
            var images  = imagepath.toString()
            val cityiddata = cityIds.toString()
            var forUpdateOrNot = check_is_lead_for_udate
            var tlId = ""

            lifecycleScope.launch {
                val res = registerViewModel.updateLead(leadID,forUpdateOrNot,name,cityiddata,images,mobile,whatsapp,decor_date,decor_time,description,total_amount
                    ,recieve_amount,address,location)

                if(res!=null){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@UpdateLeadActivity,res!!.body()!!.id, Toast.LENGTH_LONG).show()
                        tlId = res!!.body()!!.id
                    }

                    val response =  registerViewModel.sendNotification(tlId,notificationTitle,notificationBody)

                    withContext(Dispatchers.Main){
                        Toast.makeText(this@UpdateLeadActivity,response?.body()?.success.toString(), Toast.LENGTH_LONG).show()
                        finish()
                    }
                }


            }

        }


    }

    private fun observeCityViewModel() {

        registerViewModel.cityData.observe(this@UpdateLeadActivity, Observer { items ->
            adapter = CityCustomSpinnerAdapter(this,items)
            binding.spinner.adapter = adapter
        })

        var isSpinnerTouched = false
        binding.spinner.setOnTouchListener { v, event ->
            isSpinnerTouched = true
            false
        }

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (isSpinnerTouched) {
                    val selectedItem = adapter?.getItem(position)
                    selectedItem?.let {
                        cityIds = it.id.toInt()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected
            }
        }
    }
}