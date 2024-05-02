package com.example.managerapp.view.fragment.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.managerapp.R
import com.example.managerapp.databinding.ActivityPlayerBinding
import com.example.managerapp.databinding.ActivityUpdateManagerBinding
import com.example.managerapp.viewModel.RegisterViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateManagerActivity : AppCompatActivity() {

    lateinit var binding: ActivityUpdateManagerBinding

    lateinit var name: EditText
    lateinit var email: EditText
    lateinit var mobile: EditText
    lateinit var password: EditText
    lateinit var spinner: Spinner
    lateinit var submitbtn: MaterialButton
    lateinit var viewModel: RegisterViewModel
    lateinit var progressbar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        submitbtn = binding.submitButton
        name = binding.editTextName
        email = binding.editTextEmail
        mobile = binding.editTextMobile
        password = binding.editTextPassword
        progressbar = binding.progressbar

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        val idData = intent.getStringExtra("id")
        val nameData =intent.getStringExtra("name")
        val mobileData = intent.getStringExtra("mobile")
        val emailData = intent.getStringExtra("email")
        val passwordData = intent.getStringExtra("password")

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

            lifecycleScope.launch {
                val res = viewModel.updateManager(id,id, name, mobile, email, password)

                withContext(Dispatchers.Main){
                    if (res?.body()?.status == 1){
                        Toast.makeText(this@UpdateManagerActivity,res?.body()?.message, Toast.LENGTH_LONG).show()
                        getFragmentManager()?.popBackStack()
                    }else{
                        Toast.makeText(this@UpdateManagerActivity,res?.body()?.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }


    }
}