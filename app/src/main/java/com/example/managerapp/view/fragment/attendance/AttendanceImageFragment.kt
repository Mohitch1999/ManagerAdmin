package com.example.managerapp.view.fragment.attendance

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.managerapp.R
import com.example.managerapp.adapter.ManagerAttendanceAdapter
import com.example.managerapp.viewModel.RegisterViewModel
import kotlinx.coroutines.launch

class AttendanceImageFragment : Fragment() {
    lateinit var viewModel : RegisterViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view =  inflater.inflate(R.layout.fragment_attendance_image, container, false)

        val inAttendanceImage = view.findViewById<ImageView>(R.id.inAttendanceImage)
        val outAttendanceImage = view.findViewById<ImageView>(R.id.outAttendanceImage)

        val lead_status_id = arguments?.getString("id")
        val choose_id = arguments?.getString("choose_id")!!.toInt()

        val headerTitle = view.findViewById<TextView>(R.id.header_title)

        val progressbar = view.findViewById<ProgressBar>(R.id.progressBar)

        headerTitle.text = "Attendance"

        viewModel = RegisterViewModel()
        progressbar.visibility= View.VISIBLE

        lifecycleScope.launch {
            var res  = viewModel.getAttendanceLoginImage(lead_status_id.toString(),choose_id.toString())
            if(res!!.size >0){
                if (choose_id ==1){

                    if(res[0].image != null){
                        Glide.with(requireContext()).load("https://corporatelife.in/uploads/login/manager/"+ res[0].image).into(inAttendanceImage)
                    }else{
                        Glide.with(requireContext()).load(R.drawable.imageplaceholder).into(inAttendanceImage);
                    }

                    if(res[0].image2 != null){
                        Glide.with(requireContext()).load("https://corporatelife.in/uploads/login/manager/"+ res[0].image2).into(outAttendanceImage);
                    }else{
                        Glide.with(requireContext()).load(R.drawable.imageplaceholder).into(outAttendanceImage);
                    }
                }
                else if (choose_id ==2){
                    if(res[0].image != null){
                        Glide.with(requireContext()).load("https://corporatelife.in/uploads/login/team_leader/"+ res[0].image).into(inAttendanceImage)
                    }else{
                        Glide.with(requireContext()).load(R.drawable.imageplaceholder).into(inAttendanceImage);
                    }

                    if(res[0].image2 != null){
                        Glide.with(requireContext()).load("https://corporatelife.in/uploads/login/team_leader/"+ res[0].image2).into(outAttendanceImage);
                    }else{
                        Glide.with(requireContext()).load(R.drawable.imageplaceholder).into(outAttendanceImage);
                    }
                }
                else if (choose_id ==3){
                    if(res[0].image != null){
                        Glide.with(requireContext()).load("https://corporatelife.in/uploads/login/decorator/"+ res[0].image).into(inAttendanceImage)
                    }else{
                        Glide.with(requireContext()).load(R.drawable.imageplaceholder).into(inAttendanceImage);
                    }
                    if(res[0].image2 != null){
                        Glide.with(requireContext()).load("https://corporatelife.in/uploads/login/decorator/"+ res[0].image2).into(outAttendanceImage);
                    }else{
                        Glide.with(requireContext()).load(R.drawable.imageplaceholder).into(outAttendanceImage);
                    }

                }

            }
            progressbar.visibility= View.GONE
        }



        return view

    }


}