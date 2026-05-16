package com.example.virasatnamma.data.model

// ── Enums ──────────────────────────────────────────────────────────────────────

enum class TravelMode(val label: String, val emoji: String) {
    BIKE("Bike", "🏍️"),
    CAR("Car", "🚗"),
    BUS("Bus", "🚌"),
    TRAIN("Train", "🚆"),
    CAB("Cab/Taxi", "🚕"),
    FLIGHT("Flight", "✈️")
}

enum class FuelType(val label: String, val pricePerLitre: Double) {
    PETROL("Petrol", 103.0),
    DIESEL("Diesel", 90.0),
    EV("Electric", 8.0)
}

enum class HotelCategory(
    val label: String,
    val emoji: String,
    val minPrice: Int,
    val maxPrice: Int
) {
    BUDGET("Budget", "🏠", 800, 1500),
    STANDARD("Standard", "🏨", 1500, 3500),
    PREMIUM("Premium", "🏩", 3500, 7000),
    LUXURY("Luxury", "🏰", 7000, 15000)
}

enum class FoodPreference(
    val label: String,
    val emoji: String,
    val perPersonPerDay: Int
) {
    BUDGET("Budget", "🍱", 300),
    MODERATE("Moderate", "🍽️", 700),
    PREMIUM("Premium", "🍾", 1500)
}

enum class Season(val label: String) {
    NORMAL("Normal Season"),
    PEAK("Peak Season")
}

// ── Trip Plan input model ──────────────────────────────────────────────────────

data class TripPlan(
    val siteId: String = "",
    val siteName: String = "",
    val startingCity: String = "Bengaluru",
    val distanceKm: Double = 100.0,
    val numberOfTravelers: Int = 2,
    val numberOfDays: Int = 2,
    val travelMode: TravelMode = TravelMode.CAR,
    val fuelType: FuelType = FuelType.PETROL,
    val mileageKmPerLitre: Double = 15.0,
    val hotelCategory: HotelCategory = HotelCategory.STANDARD,
    val foodPreference: FoodPreference = FoodPreference.MODERATE,
    val needLocalTransport: Boolean = true,
    val needGuide: Boolean = false,
    val shoppingBudget: Double = 500.0,
    val miscBuffer: Double = 500.0,
    val isWeekend: Boolean = false,
    val season: Season = Season.NORMAL
)

// ── Detailed cost breakdown ────────────────────────────────────────────────────

data class TripCostBreakdown(
    val transport: Double = 0.0,
    val hotel: Double = 0.0,
    val food: Double = 0.0,
    val localTransport: Double = 0.0,
    val entryFees: Double = 0.0,
    val guideCost: Double = 0.0,
    val shopping: Double = 0.0,
    val miscellaneous: Double = 0.0,
    val seasonalSurge: Double = 0.0,
    val totalPerPerson: Double = 0.0,
    val grandTotal: Double = 0.0,
    val savingTips: List<String> = emptyList(),
    val budgetLevel: String = "Comfortable Trip",
    val recommendations: List<String> = emptyList()
)