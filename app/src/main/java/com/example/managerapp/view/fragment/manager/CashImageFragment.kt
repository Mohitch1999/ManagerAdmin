package com.example.managerapp.view.fragment.manager

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.managerapp.R
import com.example.managerapp.viewModel.RegisterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CashImageFragment : Fragment() {


    lateinit var viewModel: RegisterViewModel
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_cash_image, container, false)
        val cashImage = view.findViewById<ImageView>(R.id.cash_img)
        val cashImagetxt = view.findViewById<TextView>(R.id.image_txt)
        val progressbar = view.findViewById<ProgressBar>(R.id.progressBar)
        cashImagetxt.visibility = View.GONE
        val id = arguments?.getString("id")
        progressbar.visibility = View.GONE
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        lifecycleScope.launch {

            progressbar.visibility = View.VISIBLE

            val res =  viewModel.getCashImageById(id!!)
            withContext(Dispatchers.Main){
//                cashImagetxt.text = "The id is = "+res?.get(0)?.lead_id + " = " + res?.get(0)?.image

                var img = res?.get(0)?.image
                Glide.with(requireContext()).load("https://corporatelife.in/uploads/cash/$img").into(cashImage);
                progressbar.visibility = View.GONE

            }
        }






        return view
    }


}