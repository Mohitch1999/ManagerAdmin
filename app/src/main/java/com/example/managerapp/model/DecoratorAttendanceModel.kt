package com.example.managerapp.model

data class DecoratorAttendanceModel(
    val id: String,
    val in_latitude: String,
    val in_longitude: String,
    val decorator_id: String,
    val decorator_name: String,
    val out_latitude: String,
    val out_longitude: String,
    val out_time: String,
    val register_date: String,
    val register_time: String,
    val over_time: String,
    val late: String,
    val approve_hr_status: String,
)