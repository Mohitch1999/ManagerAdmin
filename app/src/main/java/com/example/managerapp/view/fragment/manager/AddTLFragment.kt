package com.example.managerapp.view.fragment.manager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.managerapp.R
import com.example.managerapp.adapter.CityCustomSpinnerAdapter
import com.example.managerapp.adapter.CustomSpinnerAdapter
import com.example.managerapp.model.CityModel
import com.example.managerapp.model.SpinnerDecoratorModel
import com.example.managerapp.utils.Prefs
import com.example.managerapp.viewModel.RegisterViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates


class AddTLFragment : Fragment() {

    lateinit var name: EditText
    lateinit var email: EditText
    lateinit var mobile: EditText
    lateinit var password: EditText
    lateinit var spinner: Spinner
    lateinit var submitbtn: MaterialButton
    lateinit var viewModel: RegisterViewModel
//    private lateinit var cityAdapter: ArrayAdapter<CityModel>
    var cityIds : Int ?  = 0

    val lists : List<CityModel>? = null
    var adapter = lists?.let { CityCustomSpinnerAdapter(requireContext(), it) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_t_l, container, false)

        submitbtn = view.findViewById<MaterialButton>(R.id.submit_button)
        name = view.findViewById<EditText>(R.id.edit_text_name)
        email = view.findViewById<EditText>(R.id.edit_text_email)
        mobile = view.findViewById<EditText>(R.id.edit_text_mobile)
        password = view.findViewById<EditText>(R.id.edit_text_password)
        spinner = view.findViewById<Spinner>(R.id.spinner)


        spinner.adapter = adapter

//        cityAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item)
//        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinner.adapter = cityAdapter

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        viewModel.fetchCityData()
        observeCityViewModel()


        submitbtn.setOnClickListener {

            var name = name.text.toString()
            var mobile = mobile.text.toString()
            var email = email.text.toString()
            var password = password.text.toString()
            var manager_id = Prefs.getManagerId().toString()
            if(name.equals("") || mobile.equals("") || email.equals("") || password.equals("")){
                Toast.makeText(requireContext(), "Please Fill All Fields !!", Toast.LENGTH_LONG).show()
            }else{
                if(cityIds == 0){
                    Toast.makeText(requireContext(), "Please select City", Toast.LENGTH_LONG)
                        .show()
                }else{
                    var city_id = cityIds.toString()
                    CoroutineScope(Dispatchers.IO).launch {
                        val res =  viewModel.createTeamLeader(manager_id,city_id, name, mobile, email, password)

                        withContext(Dispatchers.Main) {
                            Log.d("spinner",res?.body()?.message.toString())
                            if(res?.body()?.status == 1){
                                Toast.makeText(requireContext(), res?.body()?.message, Toast.LENGTH_LONG)
                                    .show()
                                getFragmentManager()?.popBackStack()
                            }else{
                                Toast.makeText(requireContext(), res?.body()?.message, Toast.LENGTH_LONG)
                                    .show()
                            }

                        }
                    }
                }
            }



        }

        return view
    }


    private fun observeCityViewModel() {
        viewModel.cityData.observe(viewLifecycleOwner, Observer { items ->

            adapter = CityCustomSpinnerAdapter(requireContext(),items)
            spinner.adapter = adapter
        })

//        var isSpinnerTouched = false
//        spinner.setOnTouchListener { v, event ->
//            isSpinnerTouched = true
//            false
//        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = adapter?.getItem(position)
                selectedItem?.let {
                    cityIds = it.id
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected
            }

        }

    }
}