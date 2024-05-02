package com.example.managerapp.view.fragment.manager

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.managerapp.R
import com.example.managerapp.utils.Prefs
import com.example.managerapp.view.fragment.decorator.ViewDecoratorLeadFragment
import com.example.managerapp.viewModel.RegisterViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ManagerLoginFragment : Fragment() {


    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var submitbtn: MaterialButton
    lateinit var headertitle: TextView
    lateinit var viewModel: RegisterViewModel

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_manager_login, container, false)

        submitbtn = view.findViewById<MaterialButton>(R.id.submit_button)
        email = view.findViewById<EditText>(R.id.edit_text_email)
        password = view.findViewById<EditText>(R.id.edit_text_password)
        headertitle = view.findViewById<EditText>(R.id.header_title)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        headertitle.text = "Login Manager"

//        val id = Prefs.getManagerId()
//        if(id!=0){
//            changeFragment(ViewLeadFragment())
//            Toast.makeText(requireContext(),"Login Successfully", Toast.LENGTH_LONG).show()
//
//        }


        submitbtn.setOnClickListener {

            submitbtn.isEnabled = false

            var email = email.text.toString()
            var password = password.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
              var r =   viewModel.loginManager(email, password)

                withContext(Dispatchers.Main){
                    submitbtn.isEnabled = true

                    if (r.toInt() != 0){
                        Prefs.setManagerId(r.toInt())
                        Toast.makeText(requireContext(),"Login Successfully", Toast.LENGTH_LONG).show()
                        changeFragment(ViewLeadFragment())
                    }else{
                        Toast.makeText(requireContext(),"Credentials are wrong", Toast.LENGTH_LONG).show()

                    }
                }
            }
        }




        return view

    }

    fun changeFragment(fragment :Fragment){
        val addLeadFragment = fragment
        val transaction=requireActivity().supportFragmentManager
            .beginTransaction()
        transaction.replace(R.id.fragment_container,addLeadFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}