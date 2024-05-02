package com.example.managerapp.view.fragment.attendance

import android.annotation.SuppressLint
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
import com.example.managerapp.adapter.CustomSpinnerAdapter
import com.example.managerapp.adapter.ManagerAttendanceAdapter
import com.example.managerapp.model.ManagerAttendanceModel
import com.example.managerapp.model.SpinnerDecoratorModel
import com.example.managerapp.utils.Prefs
import com.example.managerapp.utils.Utility
import com.example.managerapp.viewModel.RegisterViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class AttendanceFragment : Fragment() {

    lateinit var viewModel : RegisterViewModel
    private var leadListmodel : List<ManagerAttendanceModel>? = null
    val lists : List<SpinnerDecoratorModel>? = null
    var adapter = lists?.let { CustomSpinnerAdapter(requireContext(), it) }
    val newadpter = ManagerAttendanceAdapter()

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_attendance, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.leadRecycler)
        val headerTitle = view.findViewById<TextView>(R.id.header_title)
        val attendanceBtn = view.findViewById<FloatingActionButton>(R.id.attendanceBtn)
        val search = view.findViewById<FloatingActionButton>(R.id.search)
//        val approveHrBtn = view.findViewById<Button>(R.id.approveHr)
//        val over_time = view.findViewById<Button>(R.id.over_time)
//        val late = view.findViewById<Button>(R.id.late)

        val progressbar = view.findViewById<ProgressBar>(R.id.progressBar)

        headerTitle.text = "Attendance"



        val recycler = recyclerView
        recycler.adapter = newadpter
        recycler.layoutManager = LinearLayoutManager(requireContext())


        viewModel = RegisterViewModel()
        progressbar.visibility= View.VISIBLE




        viewModel.getAttendanceManager()
        viewModel.managerAttendanceData.observe(viewLifecycleOwner, Observer {
            newadpter.setData(it)
            leadListmodel = it
                    progressbar.visibility= View.GONE
        })


        search.setOnClickListener {

        }

        attendanceBtn.setOnClickListener {

            showBottomDialogByDate()
        }

        newadpter.setOnItemClickListenerLocation {
            var latitude = it.in_latitude
            var longitude = it.in_longitude
            openMap(latitude.toDouble(),longitude.toDouble())
        }

        newadpter.setOnItemClickListenerOutLocation {
            var latitude = it.out_latitude
            var longitude = it.out_longitude
            openMap(latitude.toDouble(),longitude.toDouble())
        }

        newadpter.setOnItemClickListenerImageLocation {
            val bundle = Bundle().apply {
                putString("id", it.id)
                putString("choose_id", "1")
            }
            Utility.changeFragmentWithData(AttendanceImageFragment(),requireActivity(),bundle)
        }

        newadpter.setOnItemClickListenerOverTime {
            val bundle = Bundle().apply {
                putString("id", it.id)
                putString("choose_id", "1")
            }
            Utility.changeFragmentWithData(UpdateOverTimeFragment(),requireActivity(),bundle)
        }

        return view
    }


    private fun openMap(latitude: Double, longitude: Double) {
        val strUri = "http://maps.google.com/maps?q=loc:$latitude,$longitude"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))

        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")

        startActivity(intent)
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

            val filteredItems =
                leadListmodel?.filter { it.register_date in start_date.text.toString()..end_date.text.toString() }
            if (filteredItems != null) {
                newadpter.setData(filteredItems)
            }

        }


        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }


}