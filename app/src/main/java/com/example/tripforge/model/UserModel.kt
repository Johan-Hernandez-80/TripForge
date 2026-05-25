package com.example.tripforge.model

data class User(
    val id: String,
    val email: String,
    val password: String,
    val name: String,
    val createdAt: Long = System.currentTimeMillis()
)
