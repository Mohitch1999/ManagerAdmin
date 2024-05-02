package com.example.managerapp.view.fragment.teamleader

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.managerapp.R
import com.example.managerapp.adapter.DecoratorAdapter
import com.example.managerapp.adapter.MyAdapter
import com.example.managerapp.adapter.TLAdapter
import com.example.managerapp.utils.Prefs
import com.example.managerapp.utils.Utility
import com.example.managerapp.view.fragment.decorator.LocationFragment
import com.example.managerapp.view.fragment.manager.UpdateManagerFragment
import com.example.managerapp.viewModel.RegisterViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class ViewDecoratorFragment : Fragment() {

    lateinit var viewModel: RegisterViewModel
    val newadpter = DecoratorAdapter()
    lateinit var progressbar : ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_decorator, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler)
        val headerTitle = view.findViewById<TextView>(R.id.header_title)
        progressbar = view.findViewById<ProgressBar>(R.id.progressbar)
        headerTitle.text = "View Decorator"
        progressbar.visibility = View.VISIBLE

        val tlId = Prefs.getTeamLeaderId().toString()
        val recycler = recyclerView
        recycler.adapter = newadpter
        recycler.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        lifecycleScope.launch {
            val res = viewModel.getDecoratorById(tlId)
            if(res!=null){
                newadpter.setData(res)
                progressbar.visibility = View.GONE
            }
        }




        newadpter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putString("name", it.name)
                putString("mobile", it.mobile)
                putString("id",it.id)
                putString("email",it.email)
                putString("password",it.password)
                putString("choose","3")
                putString("changeTitle","Update Decorator")
            }

            Utility.changeFragmentWithData(UpdateManagerFragment(),requireActivity(),bundle)
        }

        newadpter.setOnItemClickListenerDelete {
            showAlertDialog(it.id)
        }

        newadpter.setOnItemClickListenerLocation {
            val bundle = Bundle().apply {
                putString("id",it.id)
            }

            Utility.changeFragmentWithData(LocationFragment(),requireActivity(),bundle)
        }


        return view
    }

    fun changeFragment(fragment: Fragment) {
        val addLeadFragment = fragment
        val transaction = requireActivity().supportFragmentManager
            .beginTransaction()
        transaction.replace(R.id.fragment_container, addLeadFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun callDecoratorApi(){

        viewModel.fetchDecoratorData()

        viewModel.decoratorData.observe(viewLifecycleOwner, Observer { user ->
            newadpter.setData(user)
            progressbar.visibility = View.GONE

        })
    }

    private fun showAlertDialog(id :String) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.apply {
            setTitle("Delete Lead")
            setMessage("Are you sure you want to delete?")
            setPositiveButton("Yes") { dialog, which ->
                lifecycleScope.launch {
                    progressbar.visibility = View.VISIBLE
                    val res = viewModel.deleteLeadById(id,"3")
                    if(res?.body()?.id?.toInt() == 1){
                        showToast(res?.body()!!.message)
                        callDecoratorApi()
                    }else{
                        showToast("something wrong")
                    }
                }

            }
            setNegativeButton("No") { dialog, which ->
                // Handle negative button click
                // For example: dismiss the dialog
                dialog.dismiss()
//                showToast("Cancelled")
            }

            create().show()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}