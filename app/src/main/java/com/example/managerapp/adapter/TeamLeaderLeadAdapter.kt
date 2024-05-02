package com.example.managerapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.managerapp.R
import com.example.managerapp.model.LeadModel

class TeamLeaderLeadAdapter() : RecyclerView.Adapter<TeamLeaderLeadAdapter.ViewHolder>() {

    private var userList = emptyList<LeadModel>()
    private lateinit var context  : Context
    private var onItemClickListener: ((LeadModel) -> Unit)? = null
    private var onItemClickListenerDelete: ((LeadModel) -> Unit)? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define your ViewHolder components
        val name: TextView = itemView.findViewById(R.id.lead_name)
        val mobile: TextView = itemView.findViewById(R.id.lead_mobile)
        val decor_date: TextView = itemView.findViewById(R.id.lead_decor_date)
        val decor_time: TextView = itemView.findViewById(R.id.lead_decor_time)
        val total_amount: TextView = itemView.findViewById(R.id.lead_total_amount)
        val recieve_amount: TextView = itemView.findViewById(R.id.lead_recieve_amount)
        val status: TextView = itemView.findViewById(R.id.lead_status)
        val image: ImageView = itemView.findViewById(R.id.lead_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate your item layout and return a ViewHolder
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.team_leader_lead_card, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind data to your ViewHolder components

        val data = userList[position]
        var showstatus = ""

//        // sets the text to the textview from our itemHolder class
        holder.name.text = "Name : " +data.name
        holder.mobile.text = "Mobile : " +data.mobile
        holder.decor_date.text = "Decor Date : " +data.decor_date
        holder.decor_time.text = "Decor Time : " +data.decor_time
        holder.total_amount.text = "Total Amount : " +data.total_amount
        holder.recieve_amount.text = "Recieve Amount : " +data.recieve_amount

        if (data.status.toInt() == 5){
            holder.itemView.findViewById<CardView>(R.id.lead_card_view_row_layout) .setBackgroundResource(R.drawable.card_view_green);
        }else{
            holder.itemView.findViewById<CardView>(R.id.lead_card_view_row_layout) .setBackgroundResource(R.drawable.card_view_orange);
        }

        if (data.status.toInt() == 0){
            showstatus = "Pending"
        }else if (data.status.toInt() == 1){
            showstatus = "Leave for decoration"
        }else if (data.status.toInt() == 2){
            showstatus = "Reached at location"
        }else if (data.status.toInt() == 3){
            showstatus = "Video uploaded before decoration"
        }else if (data.status.toInt() == 4){
            showstatus = "Video uploaded after decoration"
        }else if (data.status.toInt() == 5){
            showstatus = "Completed"
        }


//        if (data.image!=null){
//            Glide.with(context).load("https://corporatelife.in/uploads/"+data.image).into(holder.image);
//        }

        holder.status.text = "Status : $showstatus"

        holder.itemView.findViewById<LinearLayout>(R.id.left_section).setOnClickListener {
            onItemClickListener?.invoke(data)

        }
//        holder.itemView.findViewById<LinearLayout>(R.id.right_section).setOnClickListener {
//            onItemClickListenerDelete?.invoke(data)
//
//        }



    }

    fun setOnItemClickListener(listener: (LeadModel) -> Unit) {
        this.onItemClickListener = listener
    }
    fun setOnItemClickListenerDelete(listener: (LeadModel) -> Unit) {
        this.onItemClickListenerDelete = listener
    }


    override fun getItemCount(): Int {
        return userList.size
    }

    fun setData(user: List<LeadModel>?,context: Context) {
        if (user != null) {
            this.userList = user
        }
        this.context = context
        notifyDataSetChanged()
    }
}