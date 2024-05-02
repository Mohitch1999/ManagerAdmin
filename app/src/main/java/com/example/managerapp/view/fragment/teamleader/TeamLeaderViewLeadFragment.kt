package com.example.managerapp.view.fragment.teamleader

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.managerapp.R
import com.example.managerapp.adapter.DecoratorAdapter
import com.example.managerapp.adapter.MyAdapter
import com.example.managerapp.adapter.TeamLeaderLeadAdapter
import com.example.managerapp.model.LeadModel
import com.example.managerapp.utils.Prefs
import com.example.managerapp.utils.Utility
import com.example.managerapp.view.activity.SelectAsLoginActivity
import com.example.managerapp.view.fragment.notification.NotificationFragment
import com.example.managerapp.viewModel.RegisterViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar


class TeamLeaderViewLeadFragment : Fragment() {

    lateinit var viewModel: RegisterViewModel
    var newadpter = TeamLeaderLeadAdapter()
    private var leadListmodel : List<LeadModel>? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_team_leader_view_lead, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.leadRecycler)
        val viewtlbtn = view.findViewById<MaterialButton>(R.id.view_tl_btn)
        val addtlbtn = view.findViewById<Button>(R.id.add_tl_btn)
        val addleadbtn = view.findViewById<Button>(R.id.add_lead_btn)
        val search = view.findViewById<FloatingActionButton>(R.id.search)
        val progressbar = view.findViewById<ProgressBar>(R.id.progressbar)
        val nodata = view.findViewById<TextView>(R.id.nodata)
        progressbar.visibility = View.VISIBLE

        val headerTitle = view.findViewById<TextView>(R.id.header_title)
        val headerNotificationBtn = view.findViewById<ImageButton>(R.id.header_btn)
        headerNotificationBtn.visibility = View.VISIBLE

        val headerLogoutBtn = view.findViewById<ImageButton>(R.id.header_btn_logout)
        headerLogoutBtn.visibility = View.VISIBLE

        headerTitle.text = "View Leads"

        val id = Prefs.getTeamLeaderId().toString()
        val token = Prefs.getTeamLeaderToken().toString()

        search.setOnClickListener {
            showBottomDialog()
        }

        headerNotificationBtn.setOnClickListener {

            val bundle = Bundle().apply {
                putString("id", id)
                putString("choose_id","2")
            }
            Utility.changeFragmentWithNotificationData(NotificationFragment(),requireActivity(),bundle)
        }

        headerLogoutBtn.setOnClickListener {
            showAlertDialogForLogout()
        }


        newadpter = TeamLeaderLeadAdapter()
        val recycler = recyclerView
        recycler.adapter = newadpter
        recycler.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)



        lifecycleScope.launch {

           val user =  viewModel.getTeamLeaderLeadById(id)
            newadpter.setData(user,requireContext())
            progressbar.visibility = View.GONE
            leadListmodel = user
        }

        lifecycleScope.launch {
            val tokenstatus = viewModel.updateTokenTeamLeader(id,token)
            Log.d("token status",tokenstatus)

        }





        newadpter.setOnItemClickListener {


            val bundle = Bundle().apply {
                putString("name", it.name)
                putString("mobile", it.mobile)
                putString("id",it.id)
                putString("whatsapp",it.whatsapp)
                putString("total",it.total_amount)
                putString("recieve",it.recieve_amount)
                putString("desc",it.description)
                putString("location",it.location)
                putString("date",it.decor_date)
                putString("register_date",it.register_date)
                putString("time",it.decor_time)
                putString("address",it.address)
                putString("tlId",it.tid)
                putString("status",it.status)
                putString("check_tl_or_decor","2")
            }

            Utility.changeFragmentWithData(ViewLeadDetailFragment(),requireActivity(),bundle)

        }

        viewtlbtn.setOnClickListener {
            Utility.changeFragment(ViewDecoratorFragment(),requireActivity())

        }


        addtlbtn.setOnClickListener {
            Utility.changeFragment(AddDecoratorFragment(),requireActivity())
        }

        return view
    }


    override fun onResume() {
        super.onResume()
        viewModel.fetchLeadData()
    }

    private fun showAlertDialogForLogout() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.apply {
            setTitle("Logout")
            setMessage("Are you sure you want to logout?")
            setPositiveButton("Yes") { dialog, which ->

                Prefs.clearall(requireContext())
                val intent = Intent(requireContext(), SelectAsLoginActivity::class.java)
                startActivity(intent)
                activity?.finish()

            }
            setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }

            create().show()
        }
    }

    private fun showBottomDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)

        // Example of interacting with views in the bottom dialog
        val viewAllBtn = bottomSheetView.findViewById<MaterialButton>(R.id.view_all_tl_btn)
        val viewPendingBtn = bottomSheetView.findViewById<MaterialButton>(R.id.view_pending_btn)
        val viewCompleteBtn = bottomSheetView.findViewById<MaterialButton>(R.id.view_complete_btn)
        val viewDateBtn = bottomSheetView.findViewById<MaterialButton>(R.id.view_date_btn)

        viewAllBtn.setOnClickListener {
            val filterdata =   leadListmodel?.filter {
                it.mobile.toInt() > 0
            }
            newadpter.setData(filterdata,requireContext())
        }

        viewPendingBtn.setOnClickListener {
            val filterdata =   leadListmodel?.filter {
                it.status.toInt() < 5
            }
            newadpter.setData(filterdata,requireContext())
        }

        viewCompleteBtn.setOnClickListener {
            val filterdata =   leadListmodel?.filter {
                it.status.toInt() == 5
            }
            newadpter.setData(filterdata,requireContext())
        }

        viewDateBtn.setOnClickListener {
            var currentDate = ""
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->

                    if(monthOfYear > 8){
                        currentDate =
                            (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                        val filterdata =   leadListmodel?.filter {
                            it.decor_date == currentDate
                        }
                        newadpter.setData(filterdata,requireContext())
                    }else{
                        currentDate =
                            (dayOfMonth.toString() + "-" + "0"+(monthOfYear + 1) + "-" + year)
                        val filterdata =   leadListmodel?.filter {
                            it.decor_date == currentDate
                        }
                        newadpter.setData(filterdata,requireContext())
                    }

                },
                year,
                month,
                day
            )
            datePickerDialog.show()

        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }
}