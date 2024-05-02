package com.example.managerapp.view.fragment.manager

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.managerapp.R
import com.example.managerapp.model.LeadModel
import com.example.managerapp.model.VideoLeadModel
import com.example.managerapp.player.PlayerActivity
import com.example.managerapp.viewModel.RegisterViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ViewLeadVideoFragment : Fragment() {

    lateinit var viewModel: RegisterViewModel
     var videoLead : List<VideoLeadModel>? = null
    var beforedecor= ""
    var afterdecor= ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_view_lead_video, container, false)

        val video1 = view.findViewById<MaterialButton>(R.id.video1)
        val video2 = view.findViewById<MaterialButton>(R.id.video2)



        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        val id = arguments?.getString("id")

        lifecycleScope.launch {
           videoLead = viewModel.getVideoById(id!!)

            if(videoLead!=null){
                beforedecor = "https://corporatelife.in/uploads/videos/"+ videoLead!![0].before_decoration
                afterdecor = "https://corporatelife.in/uploads/videos/"+ videoLead!![0].after_decoration
            }

            video1.setOnClickListener {

                val intent = Intent(requireContext(),PlayerActivity::class.java)
                intent.putExtra("videoUri",beforedecor)
                startActivity(intent)
            }

            video2.setOnClickListener {

                val intent = Intent(requireContext(),PlayerActivity::class.java)
                intent.putExtra("videoUri",afterdecor)
                startActivity(intent)
            }

        }








        return view
    }


}