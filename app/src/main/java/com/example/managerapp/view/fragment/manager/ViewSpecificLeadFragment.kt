package com.example.managerapp.view.fragment.manager

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.managerapp.NewLeadActivity
import com.example.managerapp.R
import com.example.managerapp.UpdateLeadActivity
import com.example.managerapp.utils.Utility
import com.google.android.material.button.MaterialButton

class ViewSpecificLeadFragment : Fragment() {


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_specific_lead, container, false)

        val view_image_btn: MaterialButton = view.findViewById(R.id.view_image_btn)
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
        // Retrieve other data items as needed

        // Example: Set retrieved data to TextViews
        view.findViewById<TextView>(R.id.lead_name).text = "Name : " + name
        view.findViewById<TextView>(R.id.lead_mobile).text = "Mobile : " + mobile
        view.findViewById<TextView>(R.id.lead_whatsapp).text = "Whatsapp : " + whatsapp
        view.findViewById<TextView>(R.id.lead_total_amount).text = "Total Amount : " + total
        view.findViewById<TextView>(R.id.lead_recieve_amount).text = "Recieve Amount : " + recieve
        view.findViewById<TextView>(R.id.address).text = "Address : " + address
        view.findViewById<TextView>(R.id.generate_date).text = "Register Date : " + register_date
//        view.findViewById<TextView>(R.id.location).text = "Location : " + location
        view.findViewById<TextView>(R.id.description).text = "Description : " + desc
        locations.text = "Location : " + location
        view.findViewById<TextView>(R.id.lead_decor_date).text = "Decoration Date : " + date
        view.findViewById<TextView>(R.id.lead_decor_time).text = "Decoration Time : " + time
//        view.findViewById<TextView>(R.id.lead_status).text =


        if(status?.toInt() == 4){
            update_lead.visibility = View.GONE
            view_video_btn.visibility = View.VISIBLE
            view_cash_image_btn.visibility = View.GONE
        }else if(status?.toInt() == 5){
            update_lead.visibility = View.GONE
            view_video_btn.visibility = View.VISIBLE
            view_cash_image_btn.visibility = View.VISIBLE
        }else{
            update_lead.visibility = View.VISIBLE
            view_video_btn.visibility = View.GONE
            view_cash_image_btn.visibility = View.GONE
        }

        view_cash_image_btn.setOnClickListener {
            val bundle = Bundle().apply {
                putString("id", id)
            }
            Utility.changeFragmentWithData(CashImageFragment(),requireActivity(),bundle)
        }

        view_image_btn.setOnClickListener {
            val bundle = Bundle().apply {
                putString("id", id)
                putString("verify", "1")
            }
            Utility.changeFragmentWithData(ViewLeadImagesFragment(),requireActivity(),bundle)

        }

        view_video_btn.setOnClickListener {
            val bundle = Bundle().apply {
                putString("id", id)
            }
            Utility.changeFragmentWithData(ViewLeadVideoFragment(),requireActivity(),bundle)

        }

        // use 2 for update the data with the single UI
        update_lead.setOnClickListener {
            val intent = Intent(requireContext(),UpdateLeadActivity::class.java)
            intent.putExtra("id",id)
            intent.putExtra("name",name)
            intent.putExtra("mobile",mobile)
            intent.putExtra("whatsapp",whatsapp)
            intent.putExtra("total",total)
            intent.putExtra("recieve",recieve)
            intent.putExtra("desc",desc)
            intent.putExtra("location",location)
            intent.putExtra("address",address)
            intent.putExtra("date",date)
            intent.putExtra("time",time)
            intent.putExtra("tlId",tlId)
            intent.putExtra("udpatestatus","2")
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.silde_in,R.anim.slide_out)
        }

        locations.setOnClickListener {

            openWebPage(location!!)

        }


        return view
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

    fun changeFragmentWithData(fargment :Fragment,bundle: Bundle){
        val specificLeadFragment = fargment
        specificLeadFragment.arguments = bundle

        // Navigate to the specificLeadFragment
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.silde_in, R.anim.slide_out)
            .replace(R.id.fragment_container, specificLeadFragment)
            .addToBackStack(null)
            .commit()
    }

}