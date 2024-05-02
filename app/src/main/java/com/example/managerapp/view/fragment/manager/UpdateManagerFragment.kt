package com.example.managerapp.view.fragment.manager

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.managerapp.R
import com.example.managerapp.viewModel.RegisterViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UpdateManagerFragment : Fragment() {

    lateinit var name: EditText
    lateinit var email: EditText
    lateinit var mobile: EditText
    lateinit var password: EditText
    lateinit var spinner: Spinner
    lateinit var submitbtn: MaterialButton
    lateinit var viewModel: RegisterViewModel
    lateinit var progressbar : ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_manager, container, false)

        submitbtn = view.findViewById<MaterialButton>(R.id.submit_button)
        name = view.findViewById<EditText>(R.id.edit_text_name)
        email = view.findViewById<EditText>(R.id.edit_text_email)
        mobile = view.findViewById<EditText>(R.id.edit_text_mobile)
        password = view.findViewById<EditText>(R.id.edit_text_password)
        val title = view.findViewById<TextView>(R.id.signup_txt)
        progressbar = view.findViewById<ProgressBar>(R.id.progressbar)

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        val idData = arguments?.getString("id")
        val nameData = arguments?.getString("name")
        val mobileData = arguments?.getString("mobile")
        val emailData = arguments?.getString("email")
        val passwordData = arguments?.getString("password")
        val headertitle = arguments?.getString("changeTitle")
        val chooseID = arguments?.getString("choose")
        title.text = headertitle

        name.text = Editable.Factory.getInstance().newEditable(nameData)
        mobile.text = Editable.Factory.getInstance().newEditable(mobileData)
        email.text = Editable.Factory.getInstance().newEditable(emailData)
        password.text = Editable.Factory.getInstance().newEditable(passwordData)



        submitbtn.setOnClickListener {

            progressbar.visibility = View.VISIBLE
            var name = name.text.toString()
            var mobile = mobile.text.toString()
            var email = email.text.toString()
            var password = password.text.toString()
            var id = idData.toString()
            var choose_id = chooseID!!

            lifecycleScope.launch {
                val res = viewModel.updateManager(id, choose_id, name, mobile, email, password)

                withContext(Dispatchers.Main){
                    if (res?.body()?.status == 1){
                        Toast.makeText(requireContext(),res?.body()?.message, Toast.LENGTH_LONG).show()
                        getFragmentManager()?.popBackStack()
                    }else{
                        Toast.makeText(requireContext(),res?.body()?.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }








        return view
    }

}