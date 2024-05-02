package com.example.managerapp.view.fragment.manager

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.managerapp.R
import com.example.managerapp.model.CityModel
import com.example.managerapp.viewModel.RegisterViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.properties.Delegates


class AddLeadFragment : Fragment() {

    lateinit var name: EditText
    lateinit var whatsapp: EditText
    lateinit var mobile: EditText
    lateinit var location: EditText
    lateinit var address: EditText
    lateinit var description: EditText
    lateinit var total_amount: EditText
    lateinit var recieve_amount: EditText
    lateinit var decor_date_btn: Button
    lateinit var decor_time_btn: Button
    lateinit var decor_timepicker: TimePicker
    lateinit var decor_date_txt: TextView
    lateinit var decor_time_txt: TextView
    lateinit var spinner: Spinner
    lateinit var submitbtn: MaterialButton
    lateinit var viewModel: RegisterViewModel
    private lateinit var cityAdapter: ArrayAdapter<CityModel>
    var timepick  = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_add_lead, container, false)

        submitbtn = view.findViewById<MaterialButton>(R.id.submit_button)
        name = view.findViewById<EditText>(R.id.edit_text_name)
        mobile = view.findViewById<EditText>(R.id.edit_text_mobile)
        whatsapp = view.findViewById<EditText>(R.id.edit_text_whatsapp)
        address = view.findViewById<EditText>(R.id.edit_text_address)
        location = view.findViewById<EditText>(R.id.edit_text_location)
        description = view.findViewById<EditText>(R.id.edit_text_description)

        decor_date_btn = view.findViewById<Button>(R.id.datepickbtn)
        decor_date_txt = view.findViewById<TextView>(R.id.datepicktxt)

        decor_timepicker = view.findViewById<TimePicker>(R.id.timePicker)
        decor_time_txt = view.findViewById<TextView>(R.id.timepicktxt)
        decor_time_btn = view.findViewById<Button>(R.id.timepickbtn)


        cityAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item)
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = cityAdapter

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        viewModel.fetchCityData()

        decor_date_btn.setOnClickListener {

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    decor_date_txt.text =
                        (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }


        decor_time_btn.setOnClickListener {
            if(timepick  == false){
                timepick = true
                decor_time_btn.text = "Done"
                decor_timepicker.visibility = View.VISIBLE
                decor_time_txt.visibility = View.GONE

                decor_timepicker.setOnTimeChangedListener { _, hour, minute -> var hour = hour
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
                    if (decor_time_txt != null) {
                        val hour = if (hour < 10) "0" + hour else hour
                        val min = if (minute < 10) "0" + minute else minute
                        val msg = "$hour : $min $am_pm"
                        decor_time_txt.text = msg

                    }
                }

            }else{
                timepick = false
                decor_time_btn.text = "Pick Time"
                decor_timepicker.visibility = View.GONE
                decor_time_txt.visibility = View.VISIBLE
            }
        }



        submitbtn.setOnClickListener {

            var name = name.text.toString()
            var mobile = mobile.text.toString()
            var whatsapp = whatsapp.text.toString()
            var location = location.text.toString()
            var address = address.text.toString()
            var description = description.text.toString()
            var totalamount = total_amount.text.toString()
            var recieveamount = recieve_amount.text.toString()
            var decordate = decor_date_txt.text .toString()
            var decortime = decor_time_txt.text.toString()


//            CoroutineScope(Dispatchers.IO).launch {
//                viewModel.insertLead(name,"null", mobile, whatsapp, decordate,decortime,
//                    description,totalamount,recieveamount,location,address)
//
//            }

        }

        return view
    }


}