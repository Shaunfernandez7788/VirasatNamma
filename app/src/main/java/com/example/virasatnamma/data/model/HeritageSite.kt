package com.example.virasatnamma.data.model

data class HeritageSite(
    val id: String,
    val name: String,
    val location: String,
    val description: String,
    val imageRes: Int,
    val audioRes: Int,
    val distance: Double = 10.0
)