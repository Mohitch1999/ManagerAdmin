package com.example.managerapp.view.fragment.teamleader

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.managerapp.R
import com.example.managerapp.model.CityModel
import com.example.managerapp.utils.Prefs
import com.example.managerapp.viewModel.RegisterViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates


class AddDecoratorFragment : Fragment() {


    lateinit var name: EditText
    lateinit var email: EditText
    lateinit var mobile: EditText
    lateinit var password: EditText
    lateinit var spinner: Spinner
    lateinit var submitbtn: MaterialButton
    lateinit var viewModel: RegisterViewModel
    lateinit var progressbar : ProgressBar
//    private lateinit var cityAdapter: ArrayAdapter<CityModel>
    var cityIds by Delegates.notNull<Int>()

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view =  inflater.inflate(R.layout.fragment_add_decorator, container, false)

        submitbtn = view.findViewById<MaterialButton>(R.id.submit_button)
        name = view.findViewById<EditText>(R.id.edit_text_name)
        email = view.findViewById<EditText>(R.id.edit_text_email)
        mobile = view.findViewById<EditText>(R.id.edit_text_mobile)
        password = view.findViewById<EditText>(R.id.edit_text_password)
        progressbar = view.findViewById<ProgressBar>(R.id.progressbar)


        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        val tlid = Prefs.getTeamLeaderId()

        submitbtn.setOnClickListener {

                var name = name.text.toString()
                var mobile = mobile.text.toString()
                var email = email.text.toString()
                var password = password.text.toString()
                var tl_id = tlid.toString()
            if(name.equals("") || mobile.equals("") || email.equals("") || password.equals("") || tl_id.equals("")) {
                Toast.makeText(requireContext(), "Please Fill All Fields !!", Toast.LENGTH_LONG).show()
            }else{
                progressbar.visibility = View.VISIBLE
                lifecycleScope.launch {
                    val res = viewModel.createDecorator(tl_id, name, mobile, email, password)

                    withContext(Dispatchers.Main){
                        if (res?.body()?.status == 1){
                            Toast.makeText(requireContext(),res?.body()?.message,Toast.LENGTH_LONG).show()
                            progressbar.visibility = View.GONE
                            getFragmentManager()?.popBackStack()
                        }else{
                            progressbar.visibility = View.GONE
                            Toast.makeText(requireContext(),res?.body()?.message,Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

        }

        return view
    }




}