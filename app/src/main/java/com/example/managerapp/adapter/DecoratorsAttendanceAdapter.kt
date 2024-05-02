package com.example.managerapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.managerapp.R
import com.example.managerapp.model.DecoratorAttendanceModel

class DecoratorsAttendanceAdapter() : RecyclerView.Adapter<DecoratorsAttendanceAdapter.ViewHolder>() {

    private var userList = emptyList<DecoratorAttendanceModel>()
    private var onItemClickListenerLocation: ((DecoratorAttendanceModel) -> Unit)? = null
    private var onItemClickListenerOutLocation: ((DecoratorAttendanceModel) -> Unit)? = null
    private var onItemClickListenerImageLocation: ((DecoratorAttendanceModel) -> Unit)? = null
    private var onItemClickListenerApprove: ((DecoratorAttendanceModel) -> Unit)? = null
    private var onItemClickListenerOverTime: ((DecoratorAttendanceModel) -> Unit)? = null
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define your ViewHolder components
        val title: TextView = itemView.findViewById(R.id.attendanceTitle)
        val locationIn: TextView = itemView.findViewById(R.id.locationIn)
        val locationOut: TextView = itemView.findViewById(R.id.locationOut)
        val locationInTime: TextView = itemView.findViewById(R.id.location_in_time)
        val locationOutTime: TextView = itemView.findViewById(R.id.location_out_time)
        val over_time: TextView = itemView.findViewById(R.id.over_time)
        val late: TextView = itemView.findViewById(R.id.late)
        val approveHrBtn: TextView = itemView.findViewById(R.id.approveHr)
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

        holder.title.text = data.decorator_name
        holder.locationInTime.text = data.register_date +" "+ data.register_time
        holder.locationOutTime.text = data.register_date +" "+ data.out_time

        if(data.over_time.toInt() > 0){
            holder.over_time.text = "Over Time : ${data.over_time}"
        }else{
            holder.over_time.text = "Over Time : 0"
        }

        if(data.approve_hr_status.toInt() == 1){
            holder.approveHrBtn.visibility = View.GONE
        }else{
            holder.approveHrBtn.visibility = View.VISIBLE
        }

        if(data.over_time.toInt() == 0){
            holder.approveHrBtn.visibility = View.GONE
        }

        holder.late.text = "Late : ${data.late}"

        holder.itemView.findViewById<CardView>(R.id.lead_card_view_row_layout) .setBackgroundResource(R.drawable.card_view_notification);

        holder.locationIn.setOnClickListener {
            onItemClickListenerLocation?.invoke(data)
        }

        holder.locationOut.setOnClickListener {
            onItemClickListenerOutLocation?.invoke(data)
        }

        holder.approveHrBtn.setOnClickListener {
            onItemClickListenerApprove?.invoke(data)
        }

        holder.lead_card_view_row_layout.setOnClickListener {
            onItemClickListenerImageLocation?.invoke(data)
        }

        holder.over_time.setOnClickListener {
            onItemClickListenerOverTime?.invoke(data)
        }

    }

    fun setOnItemClickListenerLocation(listener: (DecoratorAttendanceModel) -> Unit) {
        this.onItemClickListenerLocation = listener
    }

    fun setOnItemClickListenerOutLocation(listener: (DecoratorAttendanceModel) -> Unit) {
        this.onItemClickListenerOutLocation = listener
    }


    fun setOnItemClickListenerImageLocation(listener: (DecoratorAttendanceModel) -> Unit) {
        this.onItemClickListenerImageLocation = listener
    }


    fun setOnItemClickListenerApprove(listener: (DecoratorAttendanceModel) -> Unit) {
        this.onItemClickListenerApprove = listener
    }

    fun setOnItemClickListenerOverTime(listener: (DecoratorAttendanceModel) -> Unit) {
        this.onItemClickListenerOverTime = listener
    }


    override fun getItemCount(): Int {
        return userList.size
    }

    fun setData(user: List<DecoratorAttendanceModel>) {
        this.userList = user
        notifyDataSetChanged()
    }
}