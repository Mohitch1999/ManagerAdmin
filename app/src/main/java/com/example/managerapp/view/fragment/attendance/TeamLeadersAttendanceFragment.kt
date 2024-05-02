package com.example.managerapp.view.fragment.attendance

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.managerapp.R
import com.example.managerapp.adapter.TeamLeaderSpinnerAdapter
import com.example.managerapp.adapter.TeamLeadersAttendanceAdapter
import com.example.managerapp.model.DecoratorAttendanceModel
import com.example.managerapp.model.SpinnerDecoratorModel
import com.example.managerapp.model.TeamLeaderAttendanceModel
import com.example.managerapp.utils.Utility
import com.example.managerapp.viewModel.RegisterViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class TeamLeadersAttendanceFragment : Fragment() {

    lateinit var viewModel: RegisterViewModel

    val lists: List<SpinnerDecoratorModel>? = null
    var teamLeaderLists: List<TeamLeaderAttendanceModel>? = null
    var teamLeaderFilteredLists: List<TeamLeaderAttendanceModel>? = null
    var adapter = lists?.let { TeamLeaderSpinnerAdapter(requireContext(), it) }
    val newadpter = TeamLeadersAttendanceAdapter()
    lateinit var recyclerView  :RecyclerView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_team_leaders_attendance, container, false)
        recyclerView = view.findViewById<RecyclerView>(R.id.leadRecycler)
        val headerTitle = view.findViewById<TextView>(R.id.header_title)
        val search = view.findViewById<FloatingActionButton>(R.id.search)
        val searchByDate = view.findViewById<FloatingActionButton>(R.id.searchByDate)
        val progressbar = view.findViewById<ProgressBar>(R.id.progressBar)

        headerTitle.text = "Attendance"

        val recycler = recyclerView
        recycler.adapter = newadpter
        recycler.layoutManager = LinearLayoutManager(requireContext())

        progressbar.visibility = View.VISIBLE

        viewModel = RegisterViewModel()

        viewModel.getAttendanceTeamLeader()
        viewModel.teamLeaderAttendanceData.observe(viewLifecycleOwner, Observer {
            newadpter.setData(it)
            teamLeaderLists = it
            progressbar.visibility = View.GONE
        })

        search.setOnClickListener {
            showBottomDialog()
        }

        searchByDate.setOnClickListener {

            var title = "Filtered Or Non Filtered"
            var message = "have you filtered Team Leader?"

            showDialogForFilterdOrNonFilteredData(title,message)

        }

        newadpter.setOnItemClickListenerLocation {
            var latitude = it.in_latitude
            var longitude = it.in_longitude
            openMap(latitude.toDouble(), longitude.toDouble())

        }

        newadpter.setOnItemClickListenerOutLocation {
            var latitude = it.out_latitude
            var longitude = it.out_longitude
            openMap(latitude.toDouble(), longitude.toDouble())
        }

        newadpter.setOnItemClickListenerImageLocation {
            val bundle = Bundle().apply {
                putString("id", it.id)
                putString("choose_id", "2")
            }
            Utility.changeFragmentWithData(AttendanceImageFragment(), requireActivity(), bundle)
        }

        newadpter.setOnItemClickListenerOverTime {
            val bundle = Bundle().apply {
                putString("id", it.id)
                putString("over_time", it.over_time)
                putString("choose_id", "2")
            }
            Utility.changeFragmentWithData(UpdateOverTimeFragment(), requireActivity(), bundle)
        }

        newadpter.setOnItemClickListenerApprove {
            progressbar.visibility = View.VISIBLE
            lifecycleScope.launch {
                val res = viewModel.updateHrLoginStatus(it.id,"2")
                if (res?.body()?.status == 1){
                    withContext(Dispatchers.Main){
                        progressbar.visibility = View.GONE
                        Toast.makeText(requireContext(),"Approved",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }


        return view
    }

    private fun openMap(latitude: Double, longitude: Double) {
        val strUri = "http://maps.google.com/maps?q=loc:$latitude,$longitude"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
        intent.setClassName("com.google.android.apps.maps",
            "com.google.android.maps.MapsActivity")
        startActivity(intent)
    }


    @SuppressLint("MissingInflatedId")
    private fun showBottomDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_attendance_dialog, null)

        // Example of interacting with views in the bottom dialog
        val spinner = bottomSheetView.findViewById<Spinner>(R.id.spinner)
        val makeFilterBtn = bottomSheetView.findViewById<Button>(R.id.make_filter)
        val viewAllBtn = bottomSheetView.findViewById<Button>(R.id.view_all_btn)
        val viewOverTimeBtn = bottomSheetView.findViewById<Button>(R.id.view_over_time_btn)
        val viewLateBtn = bottomSheetView.findViewById<Button>(R.id.view_late_btn)
        val viewTotalBtn = bottomSheetView.findViewById<Button>(R.id.view_total_btn)

        spinner.adapter = adapter

        viewModel.getTeamLeaderForSpinner()
        viewModel.getTeamLeaderForHrSpinner.observe(viewLifecycleOwner, Observer {
            val otherData = SpinnerDecoratorModel("0", "Choose Team Leader")
            val modified = it.toMutableList()
            modified.add(0, otherData)
            adapter = TeamLeaderSpinnerAdapter(requireContext(), modified)
            spinner.adapter = adapter
        })

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
                if (isSpinnerTouched) {
                    val selectedItem = adapter?.getItem(position)
                    selectedItem?.let {

                        val currentId = it.id.toInt()
                        val filterdata = teamLeaderLists?.filter {
                            it.team_leader_id.toInt() == currentId
                        }
                        if (filterdata != null) {
                            teamLeaderFilteredLists = filterdata
                            newadpter.setData(filterdata)

                            recyclerView.adapter?.notifyDataSetChanged()
                        }
                        Toast.makeText(
                            requireContext(),
                            "Selected: ${selectedItem.name}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected
            }

        }


        makeFilterBtn.setOnClickListener {
            var title = "Filtered Or Non Filtered"
            var message = "have you filtered Team Leaders?"

            showDialogForFilterdOrNonFilteredDataUseLateAndOverTime(title,message)

            makeFilterBtn.visibility = View.GONE
            viewAllBtn.visibility = View.VISIBLE
            viewOverTimeBtn.visibility = View.VISIBLE
            viewLateBtn.visibility = View.VISIBLE
            viewTotalBtn.visibility = View.VISIBLE
        }

        viewAllBtn.setOnClickListener {

            if(NonfilteredForUseLateAndOverTime == 1){
                val filterdata =   teamLeaderFilteredLists?.filter {
                    it.id.toInt() > 0
                }
                if (filterdata != null) {
                    newadpter.setData(filterdata)
                }
            }else if(NonfilteredForUseLateAndOverTime == 0){
                val filterdata =   teamLeaderLists?.filter {
                    it.id.toInt() > 0
                }
                if (filterdata != null) {
                    newadpter.setData(filterdata)
                }
            }

        }

        viewOverTimeBtn.setOnClickListener {

            if(NonfilteredForUseLateAndOverTime == 1){
                val filterdata =   teamLeaderFilteredLists?.filter {
                    it.over_time.toInt() > 0
                }
                if (filterdata != null) {
                    newadpter.setData(filterdata)
                }
            }else if(NonfilteredForUseLateAndOverTime == 0){
                val filterdata =   teamLeaderLists?.filter {
                    it.over_time.toInt() > 0
                }
                if (filterdata != null) {
                    newadpter.setData(filterdata)
                }
            }

        }

        viewLateBtn.setOnClickListener {
            if(NonfilteredForUseLateAndOverTime == 1){
                val filterdata =   teamLeaderFilteredLists?.filter {
                    it.late.toInt() > 0
                }
                if (filterdata != null) {
                    newadpter.setData(filterdata)
                }
            }else if(NonfilteredForUseLateAndOverTime == 0){
                val filterdata =   teamLeaderLists?.filter {
                    it.late.toInt() > 0
                }
                if (filterdata != null) {
                    newadpter.setData(filterdata)
                }
            }
        }

        viewTotalBtn.setOnClickListener {
            if(teamLeaderFilteredLists!=null){
                var total_overTime = 0
                var total_late = 0
                for (i in teamLeaderFilteredLists!!){
                    total_overTime += i.over_time.toInt()
                    total_late += i.late.toInt()
                }
                var final = total_overTime - total_late

                var finalInHour = final/60
                var finalInMin = final%60

                var title = "Over Time & Late"
                var message = "Over Time : $total_overTime min\n Late : $total_late min\n Final Time : $finalInHour hr : $finalInMin min"
                showDialogForShowOverTimeAndLate(title, message)
            }else{
                Toast.makeText(requireContext(),"No data found",Toast.LENGTH_SHORT).show()
            }

        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    @SuppressLint("MissingInflatedId")
    private fun showBottomDialogByDate() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView =
            layoutInflater.inflate(R.layout.bottom_sheet_attendance_bydate_dialog, null)

        // Example of interacting with views in the bottom dialog
        val start_date = bottomSheetView.findViewById<TextView>(R.id.start_date)
        val end_date = bottomSheetView.findViewById<TextView>(R.id.end_date)
        val start_date_btn = bottomSheetView.findViewById<Button>(R.id.start_date_btn)
        val end_date_btn = bottomSheetView.findViewById<Button>(R.id.end_date_btn)
        val done_date_btn = bottomSheetView.findViewById<Button>(R.id.done_date_btn)


        start_date_btn.setOnClickListener {
            var currentDate = ""
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->

                    if (monthOfYear > 8) {

                        if (dayOfMonth > 8) {
                            start_date.text =
                                (year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth)
                        } else {
                            start_date.text = (year.toString() + "-" + (monthOfYear + 1) + "-0" + dayOfMonth)
                        }
                    } else {
                        if (dayOfMonth > 9) {
                            start_date.text =

                                (year.toString() + "-0" + (monthOfYear + 1) + "-" + dayOfMonth)
                        } else {
                            start_date.text = (year.toString() + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth)
                        }

                    }

                },
                year,
                month,
                day
            )
            datePickerDialog.show()

        }

        end_date_btn.setOnClickListener {
            var currentDate = ""
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->

                    if (monthOfYear > 8) {

                        if (dayOfMonth > 8) {
                            end_date.text =
                                (year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth)
                        } else {
                            end_date.text = (year.toString() + "-" + (monthOfYear + 1) + "-0" + dayOfMonth)
                        }
                    } else {
                        if (dayOfMonth > 9) {
                            end_date.text =

                                (year.toString() + "-0" + (monthOfYear + 1) + "-" + dayOfMonth)
                        } else {
                            end_date.text = (year.toString() + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth)
                        }

                    }

                },
                year,
                month,
                day
            )
            datePickerDialog.show()

        }


        done_date_btn.setOnClickListener {
            // Filter items based on the date range

            if(Nonfiltered == 0){
                val filteredItems =
                    teamLeaderLists?.filter { it.register_date in start_date.text.toString()..end_date.text.toString() }
                if (filteredItems != null) {
                    newadpter.setData(filteredItems)
                }
            }else if(Nonfiltered == 1){
                val filteredItems =
                    teamLeaderFilteredLists?.filter { it.register_date in start_date.text.toString()..end_date.text.toString() }
                if (filteredItems != null) {
                    newadpter.setData(filteredItems)
                }
            }

            // Print the filtered items

        }


        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }


    private fun showDialogForFilterdOrNonFilteredData(title : String,message: String) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.apply {
            setTitle(title)
            setMessage(message)
            setCancelable(false)
            setPositiveButton("Yes") { dialog, which ->
                Nonfiltered = 1
                showBottomDialogByDate()


            }
            setNegativeButton("No") { dialog, which ->
                Nonfiltered=0
                showBottomDialogByDate()

            }

            create().show()
        }
    }

    private fun showDialogForFilterdOrNonFilteredDataUseLateAndOverTime(title : String,message: String) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.apply {
            setTitle(title)
            setMessage(message)
            setCancelable(false)
            setPositiveButton("Yes") { dialog, which ->
                NonfilteredForUseLateAndOverTime = 1



            }
            setNegativeButton("No") { dialog, which ->
                NonfilteredForUseLateAndOverTime =0

            }

            create().show()
        }
    }

    private fun showDialogForShowOverTimeAndLate(title : String,message: String) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("Yes") { dialog, which ->
                dialog.dismiss()


            }
            setNegativeButton("No") { dialog, which ->
                dialog.dismiss()

            }

            create().show()
        }
    }



    companion object{
        var Nonfiltered = 0
        var NonfilteredForUseLateAndOverTime = 0
    }

}