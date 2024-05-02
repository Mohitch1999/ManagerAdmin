package com.example.managerapp.model

data class MultipleFirebaseApiDecoratorModel(
    val canonical_ids: Int,
    val failure: Int,
    val multicast_id: Long,
    val results: List<Result>,
    val success: Int
)