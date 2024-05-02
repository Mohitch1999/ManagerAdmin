package com.example.managerapp.view.fragment.teamleader

import android.R.attr.defaultValue
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.managerapp.R
import com.example.managerapp.adapter.CityCustomSpinnerAdapter
import com.example.managerapp.adapter.CustomSpinnerAdapter
import com.example.managerapp.model.CityModel
import com.example.managerapp.model.SpinnerDecoratorModel
import com.example.managerapp.utils.Prefs
import com.example.managerapp.utils.Utility
import com.example.managerapp.view.fragment.manager.CashImageFragment
import com.example.managerapp.view.fragment.manager.ViewLeadImagesFragment
import com.example.managerapp.view.fragment.manager.ViewLeadVideoFragment
import com.example.managerapp.view.fragment.manager.ViewSpecificLeadFragment
import com.example.managerapp.viewModel.RegisterViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TLAllocateLeadFragment : Fragment() {

    private lateinit var cityAdapter: ArrayAdapter<SpinnerDecoratorModel>
    lateinit var viewModel : RegisterViewModel
    lateinit var spinner: Spinner
    lateinit var arraysata : TextView
    lateinit var spinnerTitleTxt : TextView
    lateinit var leadTitleTxt : TextView
    lateinit var progressBar: ProgressBar
    val array : ArrayList<Int>? = null
    val seta = hashSetOf<Int>();
    val startArray = 0
    val notificationTitle = "New Lead Arrived"
    val notificationBody = "Team Leader allot you new lead"

    val lists : List<SpinnerDecoratorModel>? = null
    var adapter = lists?.let { CustomSpinnerAdapter(requireContext(), it) }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_t_l_allocate_lead, container, false)

        spinner = view.findViewById<Spinner>(R.id.spinner)
        arraysata = view.findViewById<TextView>(R.id.arraydata)
        spinnerTitleTxt = view.findViewById<TextView>(R.id.spinnerTItleTxt)
        leadTitleTxt = view.findViewById<TextView>(R.id.lead_title)
        progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val submit = view.findViewById<MaterialButton>(R.id.submit_button)
        val headerTitle = view.findViewById<TextView>(R.id.header_title)
        val image_btn = view.findViewById<MaterialButton>(R.id.get_images)
        val video_btn = view.findViewById<MaterialButton>(R.id.get_video)
        val view_cash_image = view.findViewById<MaterialButton>(R.id.view_cash_images_btn)

        headerTitle.text = "Allocate Lead To Decorator"


        spinner.adapter = adapter

        progressBar.visibility = View.GONE

        var leadName = ""
        var status = ""
        var leadId = 0
        val bundle = this.arguments
        if (bundle != null) {
            leadId = bundle.getInt("id", defaultValue)
            leadName = bundle.getString("name").toString()
            status = bundle.getString("status").toString()
        }

        leadTitleTxt.text = leadName

        if(status.toInt() == 4){
            spinnerTitleTxt.visibility = View.GONE
            spinner.visibility = View.GONE
            submit.visibility = View.GONE
            image_btn.visibility = View.VISIBLE
            video_btn.visibility = View.VISIBLE
            view_cash_image.visibility = View.GONE

        }else if(status.toInt() == 5){
            spinnerTitleTxt.visibility = View.GONE
            spinner.visibility = View.GONE
            submit.visibility = View.GONE
            image_btn.visibility = View.VISIBLE
            video_btn.visibility = View.VISIBLE
            view_cash_image.visibility = View.VISIBLE
        }
        else{
            video_btn.visibility = View.GONE
            view_cash_image.visibility = View.GONE
        }
        spinner.setSelection(0)
        var isSpinnerTouched = false
        spinner.setOnTouchListener { v, event ->
            isSpinnerTouched = true

            false
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(isSpinnerTouched) {
                    if(position == 0){
                        val selectedItem = adapter?.getItem(position)
                        selectedItem?.let {
                            seta.add(it.id.toInt())
                            Toast.makeText(requireContext(), "Selected: ${selectedItem.name}", Toast.LENGTH_SHORT).show()

                        }
                    }
                    val selectedItem = adapter?.getItem(position)
                    selectedItem?.let {
                        seta.add(it.id.toInt())
                        Toast.makeText(requireContext(), "Selected: ${selectedItem.name}", Toast.LENGTH_SHORT).show()

                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected
            }
            
        }

        viewModel = RegisterViewModel()

        observeDecoratorViewModel()

        var sum = 0

        view_cash_image.setOnClickListener {

            val bundle = Bundle().apply {
                putString("id",leadId.toString())
            }

            Utility.changeFragmentWithData(CashImageFragment(),requireActivity(),bundle)

        }


        image_btn.setOnClickListener {

            val bundle = Bundle().apply {
                putString("id",leadId.toString())
                putString("verify","2")
            }

            Utility.changeFragmentWithData(ViewLeadImagesFragment(),requireActivity(),bundle)

        }

        video_btn.setOnClickListener {

            val bundle = Bundle().apply {
                putString("id",leadId.toString())
            }
            Utility.changeFragmentWithData(ViewLeadVideoFragment(),requireActivity(),bundle)

        }

        submit.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            arraysata.text = ""
            for (inn in seta){
                arraysata.text = ""+arraysata.text + "," + inn.toString()
                Log.d("array",inn.toString())
            }
            val textofarray = arraysata.text.toString()
            lifecycleScope.launch(Dispatchers.IO) {
              val res =   viewModel.createDecoratorLead(textofarray,leadId.toString())
              Log.d("allot_lead",res.toString())
                if(res){
                    viewModel.sendMultipleFirebaseApi(textofarray,notificationTitle,notificationBody)
                    delay(2000)
                    getFragmentManager()?.popBackStack()
                }
            }
        }

        return view
    }

    private fun observeDecoratorViewModel() {
        progressBar.visibility = View.VISIBLE
        val tlId = Prefs.getTeamLeaderId().toString()
        lifecycleScope.launch {
           val res =  viewModel.fetchSpinnerDecoratorData(tlId)
            withContext(Dispatchers.Main){
            if(res != null){
                val otherData = SpinnerDecoratorModel("0", "Choose Decorator")
                  val modified =   res.toMutableList()
                modified.add(0, otherData)
                    adapter =  CustomSpinnerAdapter(requireContext(), modified)
                    spinner.adapter = adapter
                }
//                Toast.makeText(requireContext(),res?.size.toString() + " = Size ",Toast.LENGTH_LONG).show()
            }

            progressBar.visibility = View.GONE
        }


    }
}