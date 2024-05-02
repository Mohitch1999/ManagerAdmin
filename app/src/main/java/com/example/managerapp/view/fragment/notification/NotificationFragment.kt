package com.example.managerapp.view.fragment.notification

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.managerapp.R
import com.example.managerapp.adapter.NotificationDecoratorAdapter
import com.example.managerapp.adapter.TeamLeaderLeadAdapter
import com.example.managerapp.model.NotificationDecoratorModel
import com.example.managerapp.utils.Prefs
import com.example.managerapp.viewModel.RegisterViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationFragment : Fragment() {

    lateinit var viewModel : RegisterViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notification, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.leadRecycler)
        val headerTitle = view.findViewById<TextView>(R.id.header_title)

        val progressbar = view.findViewById<ProgressBar>(R.id.progressBar)

            headerTitle.text = "Notification"


        val newadpter = NotificationDecoratorAdapter()
        val recycler = recyclerView
        recycler.adapter = newadpter
        recycler.layoutManager = LinearLayoutManager(requireContext())


        val choose_id = arguments?.getString("choose_id")

        val id =  arguments?.getString("id").toString()

        viewModel = RegisterViewModel()

        progressbar.visibility = View.VISIBLE

        lifecycleScope.launch {
            val res : List<NotificationDecoratorModel>? = viewModel.getNotification(id,choose_id!!)
            if (res != null) {
                withContext(Dispatchers.Main){
                    newadpter.setData(res)
                    progressbar.visibility= View.GONE
                }
            }

        }







        return view
    }


}