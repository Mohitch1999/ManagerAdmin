package com.example.managerapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.managerapp.R
import com.example.managerapp.model.CityModel
import com.example.managerapp.model.SpinnerDecoratorModel

class TeamLeaderSpinnerAdapter(context: Context, items: List<SpinnerDecoratorModel>) :
    ArrayAdapter<SpinnerDecoratorModel>(context, android.R.layout.simple_spinner_item, items) {

    @SuppressLint("ResourceAsColor")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val item = getItem(position)
        var textView = view.findViewById<TextView>(android.R.id.text1)

        if (position == 0) {
            textView.isEnabled = false
            textView.text = "Choose Team Leader"
        }
        else {
            textView.text = item?.name
        }
//        textView.text = item?.name
        textView.textSize = 26.0F
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val item = getItem(position)

        val textView = view.findViewById<TextView>(android.R.id.text1)

        if (position == 0) {
            textView.text = "Choose Team Leader"
            textView.setTextColor(context.resources.getColor(android.R.color.darker_gray))
            textView.isEnabled = false
        }
        else {
            textView.text = item?.name
            textView.setTextColor(context.resources.getColor(android.R.color.holo_green_dark))
        }
        textView.textSize = 26.0F
        return view
    }
}