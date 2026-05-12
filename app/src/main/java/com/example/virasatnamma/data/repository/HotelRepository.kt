package com.example.virasatnamma.data.repository

import com.example.virasatnamma.data.model.Hotel

object HotelRepository {

    fun getNearbyHotels(): List<Hotel> {
        return listOf(
            Hotel("1", "Heritage Inn", "Bangalore", 1200.0, 3.8, 2.5),
            Hotel("2", "Royal Palace Hotel", "Mysore", 2500.0, 4.6, 1.2),
            Hotel("3", "City Comforts", "Hampi", 1800.0, 4.2, 3.0),
            Hotel("4", "Budget Stay Lodge", "Coorg", 900.0, 3.5, 4.5)
        )
    }
}