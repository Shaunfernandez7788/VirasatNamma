package com.example.virasatnamma.ui.screens.expense

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.virasatnamma.data.model.ExpenseEstimate
import com.example.virasatnamma.data.model.Hotel
import com.example.virasatnamma.viewmodel.HeritageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(
    viewModel: HeritageViewModel,
    siteId: String
) {
    val expenses by viewModel.expenses.collectAsState()
    val hotels by viewModel.hotels.collectAsState()

    val currentExpense = expenses[siteId] ?: ExpenseEstimate()
    var selectedBudget by remember { mutableStateOf("Medium") }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Smart Expense Estimator",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(16.dp))

        // Budget Dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedBudget,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Budget") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listOf("Low", "Medium", "High").forEach { budget ->
                    DropdownMenuItem(
                        text = { Text(budget) },
                        onClick = {
                            selectedBudget = budget
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { viewModel.calculateExpense(siteId, selectedBudget) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate Smart Estimate")
        }

        Spacer(Modifier.height(20.dp))

        // Expense Breakdown Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Breakdown", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(12.dp))
                ExpenseRow("✈️ Travel",  currentExpense.travel)
                ExpenseRow("🍽️ Food",    currentExpense.food)
                ExpenseRow("🏨 Stay",    currentExpense.stay)
                ExpenseRow("🛍️ Misc",   currentExpense.misc)
                Spacer(Modifier.height(10.dp))
                Divider()
                Spacer(Modifier.height(8.dp))
                Text(
                    "Total: ₹${"%.0f".format(currentExpense.total)}",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // Hotels Section
        if (hotels.isNotEmpty()) {
            Text("Nearby Hotels", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))
            hotels.forEach { hotel ->
                HotelItem(hotel) { viewModel.selectHotel(siteId, hotel) }
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun ExpenseRow(label: String, amount: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text("₹${"%.0f".format(amount)}", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun HotelItem(hotel: Hotel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(hotel.name, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text("📍 ${hotel.location}")
            Text("₹${"%.0f".format(hotel.price)} / night")
            Text("⭐ ${hotel.rating}")
            Text("${hotel.distanceKm} km from site")
        }
    }
}
