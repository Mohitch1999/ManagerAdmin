package com.example.managerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.managerapp.R
import com.example.managerapp.model.CityModel
import com.example.managerapp.model.LeadModel
import com.example.managerapp.model.TLModel

class TLAdapter() : RecyclerView.Adapter<TLAdapter.ViewHolder>() {

    private var userList = emptyList<TLModel>()
    private var cityList = emptyList<CityModel>()
    private var onItemClickListener: ((TLModel) -> Unit)? = null
    private var onItemClickListenerDelete: ((TLModel) -> Unit)? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define your ViewHolder components
        val name: TextView = itemView.findViewById(R.id.name)
        val email: TextView = itemView.findViewById(R.id.email)
        val mobile: TextView = itemView.findViewById(R.id.mobile)
        val password: TextView = itemView.findViewById(R.id.password)
        val city: TextView = itemView.findViewById(R.id.city)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate your item layout and return a ViewHolder
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tl_item, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var city = ""
        val data = userList[position]

        if(data.city_id.toInt() == 4){
            city = "City : Delhi"
        } else if(data.city_id.toInt() == 5){
            city = "City : Delhi NCR"
        }else if(data.city_id.toInt() == 6){
            city = "City : Noida"
        }

//        // sets the text to the textview from our itemHolder class
        holder.name.text = "Name : " +data.name
        holder.mobile.text = "Mobile : "+data.mobile
        holder.email.text = "Email : "+ data.email
        holder.password.text = "Password : " + data.password

        holder.city.text = city
        holder.itemView.findViewById<CardView>(R.id.team_leader_card_view_row_layout) .setBackgroundResource(R.drawable.card_view_grey);

        holder.itemView.findViewById<LinearLayout>(R.id.left_section).setOnClickListener {
            onItemClickListener?.invoke(data)

        }
        holder.itemView.findViewById<LinearLayout>(R.id.right_section).setOnClickListener {
            onItemClickListenerDelete?.invoke(data)

        }

    }

    fun setOnItemClickListener(listener: (TLModel) -> Unit) {
        this.onItemClickListener = listener
    }
    fun setOnItemClickListenerDelete(listener: (TLModel) -> Unit) {
        this.onItemClickListenerDelete = listener
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setData(user: List<TLModel>) {
        this.userList = user
        notifyDataSetChanged()
    }
}