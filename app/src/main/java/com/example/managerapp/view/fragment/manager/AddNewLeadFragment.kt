package com.example.managerapp.view.fragment.manager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.managerapp.R
import com.example.managerapp.model.CityModel
import com.example.managerapp.viewModel.RegisterViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AddNewLeadFragment : Fragment() {


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

        val view =  inflater.inflate(R.layout.fragment_add__new__lead_, container, false)


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

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

//        viewModel.fetchCityData()


        submitbtn.setOnClickListener {

            var name = name.text.toString()
            var mobile = mobile.text.toString()
            var whatsapp = whatsapp.text.toString()
            var location = location.text.toString()
            var address = address.text.toString()
            var description = description.text.toString()
            var totalamount = total_amount.text.toString()
            var recieveamount = recieve_amount.text.toString()
//            var decordate = decor_date_txt.text .toString()
//            var decortime = decor_time_txt.text.toString()

            var decordate = "mksd"
            var decortime = "mksmd"


//            lifecycleScope.launch {
//                viewModel.insertLead(name,"null", mobile, whatsapp, decordate,decortime,
//                    description,totalamount,recieveamount,location,address)
//            }

        }



        return view
    }


}