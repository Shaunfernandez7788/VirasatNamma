package com.example.virasatnamma.ui.screens.hotel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.virasatnamma.viewmodel.HeritageViewModel

@Composable
fun HotelScreen(
    viewModel: HeritageViewModel,
    siteId: String
) {
    val hotels by viewModel.hotels.collectAsState()

    LaunchedEffect(siteId) {
        viewModel.loadNearbyHotels(siteId)
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            text = "Nearby Hotels 🏨",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(hotels) { hotel ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(hotel.name, style = MaterialTheme.typography.titleMedium)
                        Text("📍 ${hotel.location}")
                        Text("₹${hotel.price} / night")
                        Text("⭐ ${hotel.rating}")
                        Text("${hotel.distanceKm} km away")
                    }
                }
            }
        }
    }
}