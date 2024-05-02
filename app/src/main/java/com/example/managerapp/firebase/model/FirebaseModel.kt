package com.example.managerapp.firebase.model

data class FirebaseModel(
    val canonical_ids: Int,
    val failure: Int,
    val multicast_id: Long,
    val results: List<Result>,
    val success: Int
)