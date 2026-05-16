package com.example.virasatnamma.ui.screens.expense

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virasatnamma.data.model.*
import com.example.virasatnamma.data.repository.TripCostCalculator
import com.example.virasatnamma.ui.theme.*
import com.example.virasatnamma.viewmodel.HeritageViewModel

// ─────────────────────────────────────────────────────────────────────────────
// MAIN SCREEN
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun ExpenseScreen(
    viewModel: HeritageViewModel,
    siteId: String
) {
    val tripPlan      by viewModel.tripPlan.collectAsState()
    val tripCost      by viewModel.tripCost.collectAsState()
    val hasCalculated by viewModel.hasCalculated.collectAsState()
    val hotels by viewModel.hotels.collectAsState()

    LaunchedEffect(siteId) {
        viewModel.initTripPlan(siteId)
        viewModel.loadNearbyHotels(siteId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Header ────────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(BrownDark, BrownMedium)))
                .padding(20.dp)
        ) {
            Column {
                Text(
                    "Trip Planner",
                    color = GoldPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                Text(
                    tripPlan.siteName.ifBlank { "Smart Expense Estimator" },
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 13.sp
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {

            // ── FORM SECTION ──────────────────────────────────────────────────
            SectionHeader(icon = Icons.Default.EditNote, title = "Trip Details")
            Spacer(Modifier.height(12.dp))

            // Starting city
            OutlinedTextField(
                value = tripPlan.startingCity,
                onValueChange = { viewModel.updateTripPlan { copy(startingCity = it) } },
                label = { Text("Starting City") },
                leadingIcon = { Icon(Icons.Default.LocationCity, null, tint = GoldPrimary) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = outlinedFieldColors()
            )

            Spacer(Modifier.height(12.dp))

            // Travelers + Days row
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StepperCard(
                    modifier = Modifier.weight(1f),
                    label = "Travelers",
                    icon = Icons.Default.People,
                    value = tripPlan.numberOfTravelers,
                    min = 1, max = 20,
                    onDecrement = {
                        if (tripPlan.numberOfTravelers > 1)
                            viewModel.updateTripPlan { copy(numberOfTravelers = numberOfTravelers - 1) }
                    },
                    onIncrement = {
                        if (tripPlan.numberOfTravelers < 20)
                            viewModel.updateTripPlan { copy(numberOfTravelers = numberOfTravelers + 1) }
                    }
                )
                StepperCard(
                    modifier = Modifier.weight(1f),
                    label = "Days",
                    icon = Icons.Default.CalendarMonth,
                    value = tripPlan.numberOfDays,
                    min = 1, max = 14,
                    onDecrement = {
                        if (tripPlan.numberOfDays > 1)
                            viewModel.updateTripPlan { copy(numberOfDays = numberOfDays - 1) }
                    },
                    onIncrement = {
                        if (tripPlan.numberOfDays < 14)
                            viewModel.updateTripPlan { copy(numberOfDays = numberOfDays + 1) }
                    }
                )
            }

            Spacer(Modifier.height(16.dp))

            // Distance
            LabelText("Distance from ${tripPlan.startingCity}: ${tripPlan.distanceKm.toInt()} km")
            Slider(
                value = tripPlan.distanceKm.toFloat(),
                onValueChange = { viewModel.updateTripPlan { copy(distanceKm = it.toDouble()) } },
                valueRange = 10f..1000f,
                steps = 0,
                colors = SliderDefaults.colors(
                    thumbColor = GoldPrimary,
                    activeTrackColor = GoldPrimary,
                    inactiveTrackColor = GoldPrimary.copy(0.25f)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // ── TRAVEL MODE ───────────────────────────────────────────────────
            SectionHeader(icon = Icons.Default.DirectionsCar, title = "Travel Mode")
            Spacer(Modifier.height(10.dp))

            ChipGrid(
                items = TravelMode.values().toList(),
                selected = tripPlan.travelMode,
                label = { "${it.emoji} ${it.label}" },
                onSelect = { viewModel.updateTripPlan { copy(travelMode = it) } }
            )

            // Fuel type (only for Bike/Car)
            AnimatedVisibility(
                visible = tripPlan.travelMode == TravelMode.BIKE ||
                        tripPlan.travelMode == TravelMode.CAR
            ) {
                Column {
                    Spacer(Modifier.height(12.dp))
                    LabelText("Fuel Type")
                    Spacer(Modifier.height(8.dp))
                    ChipGrid(
                        items = FuelType.values().toList(),
                        selected = tripPlan.fuelType,
                        label = { it.label },
                        onSelect = { viewModel.updateTripPlan { copy(fuelType = it) } }
                    )
                    Spacer(Modifier.height(12.dp))
                    LabelText("Mileage: ${tripPlan.mileageKmPerLitre.toInt()} km/L")
                    Slider(
                        value = tripPlan.mileageKmPerLitre.toFloat(),
                        onValueChange = { viewModel.updateTripPlan { copy(mileageKmPerLitre = it.toDouble()) } },
                        valueRange = 5f..60f,
                        colors = SliderDefaults.colors(
                            thumbColor = GoldPrimary,
                            activeTrackColor = GoldPrimary,
                            inactiveTrackColor = GoldPrimary.copy(0.25f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── ACCOMMODATION ─────────────────────────────────────────────────
            SectionHeader(icon = Icons.Default.Hotel, title = "Accommodation")
            Spacer(Modifier.height(10.dp))

            ChipGrid(
                items = HotelCategory.values().toList(),
                selected = tripPlan.hotelCategory,
                label = { "${it.emoji} ${it.label}\n₹${it.minPrice}–${it.maxPrice}" },
                onSelect = { viewModel.updateTripPlan { copy(hotelCategory = it) } }
            )


            Spacer(Modifier.height(16.dp))

            // ── FOOD ──────────────────────────────────────────────────────────
            SectionHeader(icon = Icons.Default.Restaurant, title = "Food Preference")
            Spacer(Modifier.height(10.dp))

            ChipGrid(
                items = FoodPreference.values().toList(),
                selected = tripPlan.foodPreference,
                label = { "${it.emoji} ${it.label}\n₹${it.perPersonPerDay}/day" },
                onSelect = { viewModel.updateTripPlan { copy(foodPreference = it) } }
            )

            Spacer(Modifier.height(16.dp))

            // ── EXTRAS ────────────────────────────────────────────────────────
            SectionHeader(icon = Icons.Default.Tune, title = "Extras & Options")
            Spacer(Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SwitchRow(
                        label = "Local transport (auto/cab)",
                        icon = Icons.Default.DirectionsCar,
                        checked = tripPlan.needLocalTransport,
                        onToggle = { viewModel.updateTripPlan { copy(needLocalTransport = it) } }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(0.2f))
                    SwitchRow(
                        label = "Professional guide",
                        icon = Icons.Default.RecordVoiceOver,
                        checked = tripPlan.needGuide,
                        onToggle = { viewModel.updateTripPlan { copy(needGuide = it) } }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(0.2f))
                    SwitchRow(
                        label = "Weekend trip",
                        icon = Icons.Default.Weekend,
                        checked = tripPlan.isWeekend,
                        onToggle = { viewModel.updateTripPlan { copy(isWeekend = it) } }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(0.2f))
                    SwitchRow(
                        label = "Peak season",
                        icon = Icons.Default.TrendingUp,
                        checked = tripPlan.season == Season.PEAK,
                        onToggle = {
                            viewModel.updateTripPlan {
                                copy(season = if (it) Season.PEAK else Season.NORMAL)
                            }
                        }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Shopping budget
            LabelText("Shopping Budget: ${TripCostCalculator.formatINR(tripPlan.shoppingBudget)}")
            Slider(
                value = tripPlan.shoppingBudget.toFloat(),
                onValueChange = { viewModel.updateTripPlan { copy(shoppingBudget = it.toDouble()) } },
                valueRange = 0f..10000f,
                colors = SliderDefaults.colors(
                    thumbColor = GoldPrimary,
                    activeTrackColor = GoldPrimary,
                    inactiveTrackColor = GoldPrimary.copy(0.25f)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Misc buffer
            LabelText("Misc Buffer: ${TripCostCalculator.formatINR(tripPlan.miscBuffer)}")
            Slider(
                value = tripPlan.miscBuffer.toFloat(),
                onValueChange = { viewModel.updateTripPlan { copy(miscBuffer = it.toDouble()) } },
                valueRange = 0f..5000f,
                colors = SliderDefaults.colors(
                    thumbColor = GoldPrimary,
                    activeTrackColor = GoldPrimary,
                    inactiveTrackColor = GoldPrimary.copy(0.25f)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            // ── CALCULATE BUTTON ──────────────────────────────────────────────
            Button(
                onClick = { viewModel.calculateTripCost() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrownDark,
                    contentColor = GoldPrimary
                )
            ) {
                Icon(Icons.Default.Calculate, null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    "Calculate Smart Estimate",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // ── RESULTS ───────────────────────────────────────────────────────
            AnimatedVisibility(
                visible = hasCalculated,
                enter = fadeIn() + expandVertically()
            ) {
                Column {

                    Spacer(Modifier.height(24.dp))

                    ResultsSection(
                        tripCost,
                        tripPlan.numberOfTravelers
                    )

                    Spacer(Modifier.height(24.dp))

                    // ── HOTEL RECOMMENDATIONS ─────────────────────────
                    if (hotels.isNotEmpty()) {

                        SectionHeader(
                            icon = Icons.Default.Hotel,
                            title = "Recommended Hotels"
                        )

                        Spacer(Modifier.height(12.dp))

                        hotels.forEach { hotel ->

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {

                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {

                                    Text(
                                        text = hotel.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = BrownDark
                                    )

                                    Spacer(Modifier.height(6.dp))

                                    Text(
                                        text = hotel.location,
                                        fontSize = 13.sp,
                                        color = BrownMedium
                                    )

                                    Spacer(Modifier.height(4.dp))

                                    Text(
                                        text = "⭐ ${hotel.rating} • ${hotel.distanceKm} km away",
                                        fontSize = 13.sp,
                                        color = BrownMedium
                                    )

                                    Spacer(Modifier.height(8.dp))

                                    Text(
                                        text = "₹${hotel.price.toInt()} per night",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 16.sp,
                                        color = GoldPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// RESULTS SECTION
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ResultsSection(cost: TripCostBreakdown, travelers: Int) {

    // ── Grand total hero card ──────────────────────────────────────────────────
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = BrownDark),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(cost.budgetLevel, color = GoldPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))
            AnimatedTotal(cost.grandTotal)
            Spacer(Modifier.height(4.dp))
            Text(
                "for $travelers traveler${if (travelers > 1) "s" else ""}",
                color = Color.White.copy(0.7f),
                fontSize = 13.sp
            )
            Spacer(Modifier.height(8.dp))
            HorizontalDivider(color = GoldPrimary.copy(0.3f))
            Spacer(Modifier.height(8.dp))
            Text(
                "${TripCostCalculator.formatINR(cost.totalPerPerson)} per person",
                color = GoldLight,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        }
    }

    Spacer(Modifier.height(20.dp))

    // ── Breakdown card ─────────────────────────────────────────────────────────
    SectionHeader(icon = Icons.Default.Receipt, title = "Cost Breakdown")
    Spacer(Modifier.height(10.dp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val items = listOf(
                BreakdownItem("✈️ Transport",      cost.transport,      Color(0xFF1976D2)),
                BreakdownItem("🏨 Hotel / Stay",   cost.hotel,          Color(0xFF7B1FA2)),
                BreakdownItem("🍽️ Food",           cost.food,           Color(0xFF388E3C)),
                BreakdownItem("🚖 Local Transport",cost.localTransport, Color(0xFFF57C00)),
                BreakdownItem("🎟️ Entry Fees",     cost.entryFees,      Color(0xFFE64A19)),
                BreakdownItem("📖 Guide",          cost.guideCost,      Color(0xFF0288D1)),
                BreakdownItem("🛍️ Shopping",       cost.shopping,       Color(0xFFAD1457)),
                BreakdownItem("🛡️ Misc / Buffer",  cost.miscellaneous,  Color(0xFF546E7A)),
                BreakdownItem("📈 Seasonal Surge", cost.seasonalSurge,  Color(0xFFC62828)),
            )

            items.forEach { item ->
                if (item.amount > 0) {
                    BreakdownRow(item, cost.grandTotal)
                    Spacer(Modifier.height(10.dp))
                }
            }

            HorizontalDivider(color = GoldPrimary.copy(0.4f), thickness = 1.5.dp)
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Grand Total", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BrownDark)
                Text(
                    TripCostCalculator.formatINR(cost.grandTotal),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = BrownDark
                )
            }
        }
    }

    Spacer(Modifier.height(20.dp))

    // ── Visual pie-style progress bars ────────────────────────────────────────
    SectionHeader(icon = Icons.Default.PieChart, title = "Expense Distribution")
    Spacer(Modifier.height(10.dp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val categories = listOf(
                Triple("Transport",       cost.transport,       Color(0xFF1976D2)),
                Triple("Stay",            cost.hotel,           Color(0xFF7B1FA2)),
                Triple("Food",            cost.food,            Color(0xFF388E3C)),
                Triple("Local Transport", cost.localTransport,  Color(0xFFF57C00)),
                Triple("Entry + Guide",   cost.entryFees + cost.guideCost, Color(0xFFE64A19)),
                Triple("Shopping",        cost.shopping,        Color(0xFFAD1457)),
                Triple("Other",           cost.miscellaneous + cost.seasonalSurge, Color(0xFF546E7A)),
            ).filter { it.second > 0 }

            categories.forEach { (label, amount, color) ->
                ExpenseProgressBar(
                    label = label,
                    amount = amount,
                    total = cost.grandTotal,
                    color = color
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }

    Spacer(Modifier.height(20.dp))

    // ── Money saving tips ──────────────────────────────────────────────────────
    if (cost.savingTips.isNotEmpty()) {
        SectionHeader(icon = Icons.Default.Lightbulb, title = "Money Saving Tips")
        Spacer(Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDE7)),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                cost.savingTips.forEach { tip ->
                    Row(
                        modifier = Modifier.padding(vertical = 5.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(tip, fontSize = 13.sp, color = Color(0xFF5D4037), lineHeight = 20.sp)
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))
    }

    // ── Smart recommendations ──────────────────────────────────────────────────
    if (cost.recommendations.isNotEmpty()) {
        SectionHeader(icon = Icons.Default.AutoAwesome, title = "Smart Recommendations")
        Spacer(Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                cost.recommendations.forEach { rec ->
                    Row(
                        modifier = Modifier.padding(vertical = 5.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(rec, fontSize = 13.sp, color = Color(0xFF1B5E20), lineHeight = 20.sp)
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// REUSABLE COMPONENTS
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SectionHeader(icon: ImageVector, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BrownDark)
    }
}

@Composable
private fun LabelText(text: String) {
    Text(text, fontSize = 13.sp, color = BrownMedium, fontWeight = FontWeight.Medium)
}

@Composable
private fun StepperCard(
    modifier: Modifier = Modifier,
    label: String,
    icon: ImageVector,
    value: Int,
    min: Int,
    max: Int,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = GoldPrimary, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text(label, fontSize = 12.sp, color = BrownMedium)
            }
            Spacer(Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(
                    onClick = onDecrement,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(if (value > min) GoldPrimary.copy(0.15f) else Color.Transparent)
                ) {
                    Icon(Icons.Default.Remove, null, tint = BrownDark, modifier = Modifier.size(16.dp))
                }
                Text(
                    "$value",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = BrownDark
                )
                IconButton(
                    onClick = onIncrement,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(if (value < max) GoldPrimary.copy(0.15f) else Color.Transparent)
                ) {
                    Icon(Icons.Default.Add, null, tint = BrownDark, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
private fun <T> ChipGrid(
    items: List<T>,
    selected: T,
    label: (T) -> String,
    onSelect: (T) -> Unit
) {
    val rows = items.chunked(3)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        rows.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                row.forEach { item ->
                    val isSelected = item == selected
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onSelect(item) },
                        color = if (isSelected) BrownDark else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(
                            1.5.dp,
                            if (isSelected) GoldPrimary else MaterialTheme.colorScheme.outline.copy(0.3f)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = label(item),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
                            color = if (isSelected) GoldPrimary else BrownDark,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            lineHeight = 16.sp
                        )
                    }
                }
                // Fill remaining columns with empty weight
                repeat(3 - row.size) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun SwitchRow(
    label: String,
    icon: ImageVector,
    checked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = GoldPrimary, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(10.dp))
        Text(label, modifier = Modifier.weight(1f), fontSize = 13.sp, color = BrownDark)
        Switch(
            checked = checked,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = GoldPrimary,
                checkedTrackColor = BrownDark,
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color.Gray.copy(0.3f)
            )
        )
    }
}

data class BreakdownItem(val label: String, val amount: Double, val color: Color)

@Composable
private fun BreakdownRow(item: BreakdownItem, total: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(item.color)
        )
        Spacer(Modifier.width(8.dp))
        Text(item.label, modifier = Modifier.weight(1f), fontSize = 13.sp, color = BrownDark)
        Text(
            TripCostCalculator.formatINR(item.amount),
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = BrownDark
        )
    }
}

@Composable
private fun ExpenseProgressBar(
    label: String,
    amount: Double,
    total: Double,
    color: Color
) {
    val fraction = if (total > 0) (amount / total).toFloat().coerceIn(0f, 1f) else 0f
    val animatedFraction by animateFloatAsState(
        targetValue = fraction,
        animationSpec = tween(800, easing = EaseOutCubic),
        label = "progress_$label"
    )

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontSize = 12.sp, color = BrownMedium)
            Text(
                "${(fraction * 100).toInt()}% · ${TripCostCalculator.formatINR(amount)}",
                fontSize = 12.sp,
                color = BrownDark,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color.copy(alpha = 0.15f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedFraction)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )
        }
    }
}

@Composable
private fun AnimatedTotal(amount: Double) {
    val animatedAmount by animateFloatAsState(
        targetValue = amount.toFloat(),
        animationSpec = tween(1000, easing = EaseOutCubic),
        label = "total_amount"
    )
    Text(
        TripCostCalculator.formatINR(animatedAmount.toDouble()),
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        letterSpacing = 0.5.sp
    )
}

@Composable
private fun outlinedFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor   = GoldPrimary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(0.4f),
    focusedLabelColor    = GoldPrimary,
    cursorColor          = GoldPrimary
)
