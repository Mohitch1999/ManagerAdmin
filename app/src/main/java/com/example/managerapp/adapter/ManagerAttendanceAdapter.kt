package com.example.managerapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.managerapp.R
import com.example.managerapp.model.DecoratorLocationModel
import com.example.managerapp.model.DecoratorModel
import com.example.managerapp.model.ManagerAttendanceModel
import com.example.managerapp.model.NotificationDecoratorModel

class ManagerAttendanceAdapter() : RecyclerView.Adapter<ManagerAttendanceAdapter.ViewHolder>() {

    private var userList = emptyList<ManagerAttendanceModel>()
    private var onItemClickListenerLocation: ((ManagerAttendanceModel) -> Unit)? = null
    private var onItemClickListenerOutLocation: ((ManagerAttendanceModel) -> Unit)? = null
    private var onItemClickListenerImageLocation: ((ManagerAttendanceModel) -> Unit)? = null
    private var onItemClickListenerApprove: ((ManagerAttendanceModel) -> Unit)? = null
    private var onItemClickListenerOverTime: ((ManagerAttendanceModel) -> Unit)? = null
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define your ViewHolder components
        val title: TextView = itemView.findViewById(R.id.attendanceTitle)
        val locationIn: TextView = itemView.findViewById(R.id.locationIn)
        val locationOut: TextView = itemView.findViewById(R.id.locationOut)
        val locationInTime: TextView = itemView.findViewById(R.id.location_in_time)
        val locationOutTime: TextView = itemView.findViewById(R.id.location_out_time)
        val over_time: TextView = itemView.findViewById(R.id.over_time)
        val late: TextView = itemView.findViewById(R.id.late)
        val approveHr: Button = itemView.findViewById(R.id.approveHr)
        val lead_card_view_row_layout: CardView = itemView.findViewById(R.id.lead_card_view_row_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate your item layout and return a ViewHolder
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.attendance_item, parent, false)

        return ViewHolder(view)

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = userList[position]

        holder.title.text = data.manager_name
        holder.over_time.text = data.over_time
        holder.locationInTime.text = data.register_date +" "+ data.register_time
        holder.locationOutTime.text = data.register_date +" "+ data.out_time


        holder.approveHr.visibility = View.GONE
        holder.over_time.visibility = View.GONE
        holder.late.visibility = View.GONE

//        if(data.over_time.toInt() > 0){
//            holder.approveHr.visibility = View.VISIBLE
//            holder.over_time.text = data.over_time
//        }else{
//            holder.approveHr.visibility = View.GONE
//            holder.over_time.visibility = View.GONE
//        }

//        if(data.approve_hr_status.toInt() > 0){
//            holder.approveHr.visibility = View.GONE
//        }else{
//            holder.approveHr.visibility = View.VISIBLE
//        }

        holder.late.text = data.late

        holder.itemView.findViewById<CardView>(R.id.lead_card_view_row_layout).setBackgroundResource(R.drawable.card_view_notification);

        holder.locationIn.setOnClickListener {
            onItemClickListenerLocation?.invoke(data)
        }

        holder.locationOut.setOnClickListener {
            onItemClickListenerOutLocation?.invoke(data)
        }

        holder.lead_card_view_row_layout.setOnClickListener {
            onItemClickListenerImageLocation?.invoke(data)
        }

        holder.approveHr.setOnClickListener {
            onItemClickListenerApprove?.invoke(data)
        }

        holder.over_time.setOnClickListener {
            onItemClickListenerOverTime?.invoke(data)
        }
    }

    fun setOnItemClickListenerLocation(listener: (ManagerAttendanceModel) -> Unit) {
        this.onItemClickListenerLocation = listener
    }

    fun setOnItemClickListenerOutLocation(listener: (ManagerAttendanceModel) -> Unit) {
        this.onItemClickListenerOutLocation = listener
    }


    fun setOnItemClickListenerImageLocation(listener: (ManagerAttendanceModel) -> Unit) {
        this.onItemClickListenerImageLocation = listener
    }

    fun setOnItemClickListenerApprove(listener: (ManagerAttendanceModel) -> Unit) {
        this.onItemClickListenerApprove = listener
    }

    fun setOnItemClickListenerOverTime(listener: (ManagerAttendanceModel) -> Unit) {
        this.onItemClickListenerOverTime = listener
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setData(user: List<ManagerAttendanceModel>) {
        this.userList = user
        notifyDataSetChanged()
    }
}