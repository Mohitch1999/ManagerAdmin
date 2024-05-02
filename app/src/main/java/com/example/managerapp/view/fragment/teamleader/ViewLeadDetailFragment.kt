package com.example.managerapp.view.fragment.teamleader

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.managerapp.R
import com.example.managerapp.utils.Prefs
import com.example.managerapp.utils.Utility
import com.example.managerapp.view.fragment.decorator.UpdateLeadStatusFragment
import com.example.managerapp.view.fragment.notification.NotificationFragment
import com.google.android.material.button.MaterialButton


class ViewLeadDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_view_lead_detail, container, false)


        val allot_lead_btn: MaterialButton = view.findViewById(R.id.allot_lead_btn)
        val view_cash_image_btn: MaterialButton = view.findViewById(R.id.view_cash_images_btn)
        val headerTitle = view.findViewById<TextView>(R.id.header_title)
        val update_lead = view.findViewById<MaterialButton>(R.id.update_lead_btn)
        val view_video_btn = view.findViewById<MaterialButton>(R.id.view_video_btn)
        headerTitle.text = "View Lead Details"
        val locations = view.findViewById<TextView>(R.id.location)


        val name = arguments?.getString("name")
        val mobile = arguments?.getString("mobile")
        val id = arguments?.getString("id")
        val whatsapp = arguments?.getString("whatsapp")
        val total = arguments?.getString("total")
        val recieve = arguments?.getString("recieve")
        val desc = arguments?.getString("desc")
        val location = arguments?.getString("location")
        val date = arguments?.getString("date")
        val register_date = arguments?.getString("register_date")
        val time = arguments?.getString("time")
        val address = arguments?.getString("address")
        val tlId = arguments?.getString("tlId")
        val status = arguments?.getString("status")
        val check_tl_or_decor = arguments?.getString("check_tl_or_decor")!!.toInt()

        view.findViewById<TextView>(R.id.lead_name).text = "Name : " + name
        view.findViewById<TextView>(R.id.lead_mobile).text = "Mobile : " + mobile
        view.findViewById<TextView>(R.id.lead_whatsapp).text = "Whatsapp : " + whatsapp
        view.findViewById<TextView>(R.id.lead_total_amount).text = "Total Amount : " + total
        view.findViewById<TextView>(R.id.lead_recieve_amount).text = "Recieve Amount : " + recieve
        view.findViewById<TextView>(R.id.address).text = "Address : " + address
        view.findViewById<TextView>(R.id.generate_date).text = "Register Date : " + register_date
        view.findViewById<TextView>(R.id.description).text = "Description : " + desc
        locations.text = "Location : " + location
        view.findViewById<TextView>(R.id.lead_decor_date).text = "Decoration Date : " + date
        view.findViewById<TextView>(R.id.lead_decor_time).text = "Decoration Time : " + time




        locations.setOnClickListener {

            openWebPage(location!!)

        }

        allot_lead_btn.setOnClickListener {

            if(check_tl_or_decor == 2){
                val bundle = Bundle()
                bundle.putInt("id",id!!.toInt())
                bundle.putString("name",name)
                bundle.putString("status",status)

                Utility.changeFragmentWithData(TLAllocateLeadFragment(),requireActivity(),bundle)
            }
            else if(check_tl_or_decor == 3){

            val bundle = Bundle()
            bundle.putInt("id",id!!.toInt())
            bundle.putString("name",name)
            bundle.putString("total_amount",total)
            bundle.putString("recieve_amount",recieve)
            bundle.putString("status",status)

                Utility.changeFragmentWithData(UpdateLeadStatusFragment(),requireActivity(),bundle)

            }

        }

    return  view
    }

    @SuppressLint("WrongConstant")
    fun openWebPage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        val activityInfo = intent.resolveActivityInfo(requireActivity().packageManager, intent.flags)
        if (activityInfo?.exported == true) {
            startActivity(intent)
        } else {
            Toast.makeText(
                requireContext(),
                "No application that can handle this link found",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}