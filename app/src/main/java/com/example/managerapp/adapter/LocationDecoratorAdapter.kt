package com.example.managerapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.managerapp.R
import com.example.managerapp.model.DecoratorLocationModel
import com.example.managerapp.model.DecoratorModel
import com.example.managerapp.model.NotificationDecoratorModel

class LocationDecoratorAdapter() : RecyclerView.Adapter<LocationDecoratorAdapter.ViewHolder>() {

    private var userList = emptyList<DecoratorLocationModel>()
    private var onItemClickListenerLocation: ((DecoratorLocationModel) -> Unit)? = null
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define your ViewHolder components
        val title: TextView = itemView.findViewById(R.id.locationTitle)
        val body: TextView = itemView.findViewById(R.id.locationStatus)
        val date: TextView = itemView.findViewById(R.id.notificationTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate your item layout and return a ViewHolder
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.location_item, parent, false)

        return ViewHolder(view)

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = userList[position]
    var lead_status = ""
        if (data.lead_status.toInt() == 0){
            lead_status = "Pending"
        }else if (data.lead_status.toInt() == 1){
            lead_status = "Leave for decoration"
        }else if (data.lead_status.toInt() == 2){
            lead_status = "Reached at location"
        }else if (data.lead_status.toInt() == 3){
            lead_status = "Video uploaded before decoration"
        }else if (data.lead_status.toInt() == 4){
            lead_status = "Video uploaded after decoration"
        }else if (data.lead_status.toInt() == 5){
            lead_status = "Completed"
        }

        holder.title.text = data.lead_name
        holder.body.text = lead_status
        holder.date.text = data.register_date +" "+ data.register_time

        holder.itemView.findViewById<CardView>(R.id.lead_card_view_row_layout) .setBackgroundResource(R.drawable.card_view_notification);

        holder.itemView.setOnClickListener {
            onItemClickListenerLocation?.invoke(data)
        }
    }

    fun setOnItemClickListenerLocation(listener: (DecoratorLocationModel) -> Unit) {
        this.onItemClickListenerLocation = listener
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setData(user: List<DecoratorLocationModel>) {
        this.userList = user
        notifyDataSetChanged()
    }
}