package com.example.managerapp.view.fragment.decorator

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.managerapp.R
import com.example.managerapp.adapter.LocationDecoratorAdapter
import com.example.managerapp.viewModel.RegisterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LocationFragment : Fragment() {

    lateinit var viewModel : RegisterViewModel


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_location, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.leadRecycler)
        val headerTitle = view.findViewById<TextView>(R.id.header_title)

        val progressbar = view.findViewById<ProgressBar>(R.id.progressBar)

        headerTitle.text = "Locations"

        val newadpter = LocationDecoratorAdapter()
        val recycler = recyclerView
        recycler.adapter = newadpter
        recycler.layoutManager = LinearLayoutManager(requireContext())

        val id =  arguments?.getString("id").toString()

        viewModel = RegisterViewModel()

        lifecycleScope.launch {
            progressbar.visibility= View.VISIBLE
            var res  = viewModel.getDecoratorLocationById(id)
            if (res != null) {
                withContext(Dispatchers.Main){
                    newadpter.setData(res)
                    progressbar.visibility= View.GONE
                }
            }
        }


        newadpter.setOnItemClickListenerLocation {
            var latitude = it.latitude
            var longitude = it.longitude
            openMap(latitude.toDouble(),longitude.toDouble())

        }


        return view
    }

    private fun openMap(latitude: Double, longitude: Double) {
        val strUri = "http://maps.google.com/maps?q=loc:$latitude,$longitude"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))

        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")

        startActivity(intent)
    }
}