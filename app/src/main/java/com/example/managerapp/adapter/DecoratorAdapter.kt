package com.example.managerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.managerapp.R
import com.example.managerapp.model.DecoratorModel
import com.example.managerapp.model.LeadModel
import com.example.managerapp.model.TLModel

class DecoratorAdapter() : RecyclerView.Adapter<DecoratorAdapter.ViewHolder>() {

    private var userList = emptyList<DecoratorModel>()
    private var onItemClickListener: ((DecoratorModel) -> Unit)? = null
    private var onItemClickListenerDelete: ((DecoratorModel) -> Unit)? = null
    private var onItemClickListenerLocation: ((DecoratorModel) -> Unit)? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define your ViewHolder components
        val name: TextView = itemView.findViewById(R.id.name)
        val email: TextView = itemView.findViewById(R.id.email)
        val mobile: TextView = itemView.findViewById(R.id.mobile)
        val password: TextView = itemView.findViewById(R.id.password)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate your item layout and return a ViewHolder
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.decorator_item, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind data to your ViewHolder components

        var city = ""
        val data = userList[position]


//        // sets the text to the textview from our itemHolder class
        holder.name.text = "Name : " +data.name
        holder.mobile.text = "Mobile : "+data.mobile
        holder.email.text = "Email : "+ data.email
        holder.password.text = "Password : " + data.password

        holder.itemView.findViewById<CardView>(R.id.team_leader_card_view_row_layout) .setBackgroundResource(R.drawable.card_view_grey);


        holder.itemView.findViewById<LinearLayout>(R.id.left_section).setOnClickListener {
            onItemClickListener?.invoke(data)

        }
        holder.itemView.findViewById<ImageView>(R.id.lead_image).setOnClickListener {
            onItemClickListenerDelete?.invoke(data)

        }

        holder.itemView.findViewById<ImageView>(R.id.location_image).setOnClickListener {
            onItemClickListenerLocation?.invoke(data)

        }

    }

    fun setOnItemClickListener(listener: (DecoratorModel) -> Unit) {
        this.onItemClickListener = listener
    }
    fun setOnItemClickListenerDelete(listener: (DecoratorModel) -> Unit) {
        this.onItemClickListenerDelete = listener
    }

    fun setOnItemClickListenerLocation(listener: (DecoratorModel) -> Unit) {
        this.onItemClickListenerLocation = listener
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setData(user: List<DecoratorModel>) {
        this.userList = user
        notifyDataSetChanged()
    }
}