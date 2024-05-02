package com.example.managerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.managerapp.R
import com.example.managerapp.model.NotificationDecoratorModel

class NotificationDecoratorAdapter() : RecyclerView.Adapter<NotificationDecoratorAdapter.ViewHolder>() {

    private var userList = emptyList<NotificationDecoratorModel>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define your ViewHolder components
        val title: TextView = itemView.findViewById(R.id.notificationTitle)
        val body: TextView = itemView.findViewById(R.id.notificationBody)
        val date: TextView = itemView.findViewById(R.id.notificationTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate your item layout and return a ViewHolder
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_item, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = userList[position]

        holder.title.text = data.title
        holder.body.text = data.body
        holder.date.text = data.register_date +" "+ data.register_time

        holder.itemView.findViewById<CardView>(R.id.lead_card_view_row_layout) .setBackgroundResource(R.drawable.army_card_view);

    }


    override fun getItemCount(): Int {
        return userList.size
    }

    fun setData(user: List<NotificationDecoratorModel>) {
        this.userList = user
        notifyDataSetChanged()
    }
}