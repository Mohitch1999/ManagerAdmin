package com.example.managerapp.view.fragment.attendance

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.managerapp.R
import com.example.managerapp.viewModel.RegisterViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class
UpdateOverTimeFragment : Fragment() {

    lateinit var viewModel : RegisterViewModel

    var itemId = ""
    var choose_id = ""
    var over_time_txt = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_update_over_time, container, false)

        val overTime = view.findViewById<EditText>(R.id.edit_text_overtime)
        var progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val submitBtn = view.findViewById<MaterialButton>(R.id.submit_button)

        progressBar.visibility = View.GONE

        itemId = arguments?.getString("id").toString()
        over_time_txt = arguments?.getString("over_time").toString()
        choose_id = arguments?.getString("choose_id").toString()

        overTime.text = Editable.Factory.getInstance().newEditable(over_time_txt)

        viewModel = RegisterViewModel()

        submitBtn.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            var overTimes = overTime.text.toString()
            lifecycleScope.launch {
                var res = viewModel.updateOverTime(itemId,choose_id,overTimes)
                if(res?.body()?.status == 1){
                    withContext(Dispatchers.Main){
                        progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(),"Over Time Updated",Toast.LENGTH_SHORT).show()
                        fragmentManager?.popBackStack()
                    }
                }else {
                    withContext(Dispatchers.Main) {
                        progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), res?.body()?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }



        return view
    }


}