package com.example.managerapp.view.fragment.manager

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
import com.example.managerapp.viewModel.RegisterViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class ViewTLFragment : Fragment() {

    lateinit var viewModel: RegisterViewModel
    val newadpter = TLAdapter()
    lateinit var progressbar : ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_t_l, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler)
        val headerTitle = view.findViewById<TextView>(R.id.header_title)
        progressbar = view.findViewById<ProgressBar>(R.id.progressBar)
        headerTitle.text = "View Team Leaders"

        val recycler = recyclerView
        recycler.adapter = newadpter
        recycler.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        callTeamLeaderApi()

        newadpter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putString("name", it.name)
                putString("mobile", it.mobile)
                putString("id",it.id)
                putString("email",it.email)
                putString("password",it.password)
                putString("choose","2")
                putString("changeTitle","Update Team Leader")
            }

            val specificLeadFragment = UpdateManagerFragment()
            specificLeadFragment.arguments = bundle

            // Navigate to the specificLeadFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, specificLeadFragment)
                .addToBackStack(null)
                .commit()
        }

        newadpter.setOnItemClickListenerDelete {
            showAlertDialog(it.id)
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

    @SuppressLint("SuspiciousIndentation")
    fun callTeamLeaderApi(){
    progressbar.visibility = View.VISIBLE
        viewModel.fetchTLData()

        viewModel.tlData.observe(viewLifecycleOwner, Observer { user ->
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
                    val res = viewModel.deleteLeadById(id,"2")
                    if(res?.body()?.id?.toInt() == 1){
                        showToast(res?.body()!!.message)
                        callTeamLeaderApi()
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