package com.example.managerapp.model

import retrofit2.Response

data class LeadModel(
    val address: String,
    val decor_date: String,
    val register_date: String,
    val decor_time: String,
    val description: String,
    val image: String,
    val tid: String,
    val id: String,
    val location: String,
    val mobile: String,
    val name: String,
    val recieve_amount: String,
    val total_amount: String,
    val whatsapp: String,
    val status : String
)