package com.example.managerapp.view.fragment.manager

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.managerapp.R
import com.example.managerapp.viewModel.RegisterViewModel


class ViewManagerFragment : Fragment() {

    lateinit var viewModel: RegisterViewModel
    var iddata = ""
    var namedata = ""
    var emaildata = ""
    var mobiledata = ""
    var passworddata = ""

    lateinit var name : TextView
    lateinit var mobile : TextView
    lateinit var email : TextView
    lateinit var password : TextView
    lateinit var progressbar : ProgressBar
    lateinit var cardLayout : CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_view_manager, container, false)

        val headerTitle = view.findViewById<TextView>(R.id.header_title)
         name = view.findViewById<TextView>(R.id.name)
         mobile = view.findViewById<TextView>(R.id.mobile)
         email = view.findViewById<TextView>(R.id.email)
         password = view.findViewById<TextView>(R.id.password)
         cardLayout = view.findViewById<CardView>(R.id.team_leader_card_view_row_layout)
        var deleteImg = view.findViewById<ImageView>(R.id.lead_image)
        val city = view.findViewById<TextView>(R.id.city)
        val layout_id = view.findViewById<CardView>(R.id.team_leader_card_view_row_layout)

        deleteImg.visibility = View.GONE
        cardLayout.visibility = View.GONE
        headerTitle.text = "View Manager"
        city.visibility = View.GONE

         progressbar = view.findViewById(R.id.progressBar)
        progressbar.visibility = View.VISIBLE

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        progressbar.visibility = View.VISIBLE
        viewModel.fetchManager()
        fetchData()

        view.findViewById<CardView>(R.id.team_leader_card_view_row_layout).setBackgroundResource(R.color.dialog_green);
        view.findViewById<CardView>(R.id.team_leader_card_view_row_layout).setBackgroundResource(R.drawable.army_card_view);


        layout_id.setOnClickListener {

            val bundle = Bundle().apply {
                putString("name", namedata)
                putString("mobile", mobiledata)
                putString("id",iddata)
                putString("email",emaildata)
                putString("password",passworddata)
                putString("choose","1")
                putString("changeTitle","Update Manager")
            }


            val specificLeadFragment = UpdateManagerFragment()
            specificLeadFragment.arguments = bundle

            // Navigate to the specificLeadFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, specificLeadFragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    fun fetchData()
    {
        viewModel.manager.observe(viewLifecycleOwner, Observer {

            iddata = it[0].id
            namedata = it[0].name
            mobiledata = it[0].mobile
            emaildata = it[0].email
            passworddata = it[0].password

            cardLayout.visibility = View.VISIBLE

            name.text = "Name : " + namedata
            mobile.text = "Mobile : " + mobiledata
            email.text = "Email : " + emaildata
            password.text = "Password : " + passworddata

            progressbar.visibility = View.GONE
        })
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    fun showLoadingDialog(): ProgressDialog {
        val mProgressDialog = ProgressDialog(requireContext())
//        mProgressDialog.setTitle("Please wait...")
        mProgressDialog.setMessage("Loading")
        mProgressDialog.setCancelable(false)
        mProgressDialog.show()
        return mProgressDialog
    }
}