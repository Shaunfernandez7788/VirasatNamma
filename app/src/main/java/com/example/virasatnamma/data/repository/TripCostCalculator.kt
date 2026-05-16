package com.example.virasatnamma.data.repository

import com.example.virasatnamma.data.model.FuelType
import com.example.virasatnamma.data.model.TravelMode
import com.example.virasatnamma.data.model.TripCostBreakdown
import com.example.virasatnamma.data.model.TripPlan
import kotlin.math.roundToInt

object TripCostCalculator {

    // ── Entry fees per site (ASI approximate) ─────────────────────────────────
    private val entryFeesBySite = mapOf(
        "site_001" to 0.0,    // Hampi – free
        "site_002" to 70.0,   // Mysore Palace
        "site_003" to 25.0,   // Badami Caves
        "site_004" to 25.0,   // Gol Gumbaz
        "site_005" to 0.0,    // Belur – free
        "site_006" to 25.0,   // Halebidu
        "site_007" to 25.0,   // Chitradurga Fort
        "site_008" to 15.0,   // Srirangapatna
        "site_009" to 25.0,   // Pattadakal
        "site_010" to 25.0,   // Aihole
        "site_011" to 230.0,  // Bangalore Palace
        "site_012" to 25.0    // Bidar Fort
    )

    fun calculate(plan: TripPlan): TripCostBreakdown {
        val travelers = plan.numberOfTravelers.toDouble()
        val days      = plan.numberOfDays.toDouble()
        val dist      = plan.distanceKm
        val roundTrip = dist * 2

        // 1. TRANSPORT ─────────────────────────────────────────────────────────
        val baseTransport = calculateTransport(plan, roundTrip, travelers)

        // 2. HOTEL ─────────────────────────────────────────────────────────────
        val roomsNeeded = Math.ceil(travelers / 2.0)
        val nights      = (days - 1).coerceAtLeast(1.0)
        val baseHotelPerNight = plan.hotelCategory.let {
            ((it.minPrice + it.maxPrice) / 2).toDouble()
        }
        val baseHotel = baseHotelPerNight * roomsNeeded * nights

        // 3. FOOD ──────────────────────────────────────────────────────────────
        val baseFood = plan.foodPreference.perPersonPerDay * travelers * days

        // 4. LOCAL TRANSPORT ───────────────────────────────────────────────────
        val localTransport = if (plan.needLocalTransport) {
            val autoPerDay = when {
                travelers <= 2 -> 400.0
                travelers <= 4 -> 600.0
                else           -> 900.0
            }
            autoPerDay * days
        } else 0.0

        // 5. ENTRY FEES ────────────────────────────────────────────────────────
        val feePerPerson = entryFeesBySite[plan.siteId] ?: 25.0
        val entryFees = feePerPerson * travelers

        // 6. GUIDE COST ────────────────────────────────────────────────────────
        val guideCost = if (plan.needGuide) {
            val guidePerDay = when {
                travelers <= 4 -> 600.0
                travelers <= 8 -> 1000.0
                else           -> 1500.0
            }
            guidePerDay * days
        } else 0.0

        // 7. SHOPPING ─────────────────────────────────────────────────────────
        val shopping = plan.shoppingBudget

        // 8. MISCELLANEOUS ────────────────────────────────────────────────────
        val misc = plan.miscBuffer

        // 9. SEASONAL SURGE ───────────────────────────────────────────────────
        val surgeMultiplier = when {
            plan.isWeekend && plan.season == com.example.virasatnamma.data.model.Season.PEAK -> 0.30
            plan.isWeekend -> 0.15
            plan.season == com.example.virasatnamma.data.model.Season.PEAK -> 0.20
            else -> 0.0
        }
        val subTotal = baseHotel + baseTransport
        val surge = subTotal * surgeMultiplier

        // 10. TOTALS ──────────────────────────────────────────────────────────
        val grandTotal = baseTransport + baseHotel + baseFood +
                localTransport + entryFees + guideCost + shopping + misc + surge

        val perPerson = grandTotal / travelers

        // 11. BUDGET LEVEL ────────────────────────────────────────────────────
        val budgetLevel = when {
            perPerson < 3000  -> "🎒 Budget Trip"
            perPerson < 8000  -> "😊 Comfortable Trip"
            perPerson < 15000 -> "💼 Premium Trip"
            else              -> "👑 Luxury Trip"
        }

        // 12. SAVING TIPS ─────────────────────────────────────────────────────
        val tips = buildSavingTips(plan, grandTotal)

        // 13. SMART RECOMMENDATIONS ───────────────────────────────────────────
        val recommendations = buildRecommendations(plan, perPerson)

        return TripCostBreakdown(
            transport     = baseTransport.roundTo2(),
            hotel         = baseHotel.roundTo2(),
            food          = baseFood.roundTo2(),
            localTransport= localTransport.roundTo2(),
            entryFees     = entryFees.roundTo2(),
            guideCost     = guideCost.roundTo2(),
            shopping      = shopping.roundTo2(),
            miscellaneous = misc.roundTo2(),
            seasonalSurge = surge.roundTo2(),
            totalPerPerson= perPerson.roundTo2(),
            grandTotal    = grandTotal.roundTo2(),
            savingTips    = tips,
            budgetLevel   = budgetLevel,
            recommendations = recommendations
        )
    }

    // ── Transport calculator ──────────────────────────────────────────────────

    private fun calculateTransport(plan: TripPlan, roundTrip: Double, travelers: Double): Double {
        return when (plan.travelMode) {
            TravelMode.BIKE -> {
                val fuelCost = when (plan.fuelType) {
                    FuelType.EV -> (roundTrip / 6.0) * plan.fuelType.pricePerLitre  // kWh
                    else        -> (roundTrip / plan.mileageKmPerLitre) * plan.fuelType.pricePerLitre
                }
                val tollParking = roundTrip * 0.5
                fuelCost + tollParking
            }

            TravelMode.CAR -> {
                val fuelCost = when (plan.fuelType) {
                    FuelType.EV -> (roundTrip / 7.0) * plan.fuelType.pricePerLitre
                    else        -> (roundTrip / plan.mileageKmPerLitre) * plan.fuelType.pricePerLitre
                }
                val tollParking = roundTrip * 2.5
                val carsNeeded = Math.ceil(travelers / 4.0)
                (fuelCost + tollParking) * carsNeeded
            }

            TravelMode.BUS -> {
                // KSRTC approx ₹1.2 per km per person
                roundTrip * 1.2 * travelers
            }

            TravelMode.TRAIN -> {
                // Sleeper ₹0.8/km, 3AC ₹1.8/km, general ₹0.5/km
                val ratePerKm = 1.2  // average
                roundTrip * ratePerKm * travelers
            }

            TravelMode.CAB -> {
                // Base ₹100 + ₹14/km
                val cabs = Math.ceil(travelers / 4.0)
                (100.0 + roundTrip * 14.0) * cabs
            }

            TravelMode.FLIGHT -> {
                // Domestic Karnataka rough estimate
                val baseTicket = when {
                    plan.distanceKm < 300 -> 2500.0
                    plan.distanceKm < 600 -> 4000.0
                    else                  -> 6000.0
                }
                baseTicket * travelers * 2  // return
            }
        }
    }

    // ── Saving tips ────────────────────────────────────────────────────────────

    private fun buildSavingTips(plan: TripPlan, total: Double): List<String> {
        val tips = mutableListOf<String>()

        if (plan.isWeekend)
            tips.add("📅 Traveling on weekdays can save ~15% on hotels & transport.")

        if (plan.season == com.example.virasatnamma.data.model.Season.PEAK)
            tips.add("🌤️ Visiting in off-season can save up to 20% on accommodation.")

        if (plan.travelMode == TravelMode.CAB)
            tips.add("🚌 KSRTC buses cost ~60% less than cabs for the same route.")

        if (plan.travelMode == TravelMode.FLIGHT && plan.distanceKm < 400)
            tips.add("🚆 Train travel for this distance is 70% cheaper than flights.")

        if (plan.hotelCategory == com.example.virasatnamma.data.model.HotelCategory.LUXURY)
            tips.add("🏨 Switching to Premium category hotels saves ₹${formatINR((7000 * (plan.numberOfDays - 1)).toDouble())} on this trip.")

        if (plan.foodPreference == com.example.virasatnamma.data.model.FoodPreference.PREMIUM)
            tips.add("🍱 Choosing moderate dining saves ₹${formatINR(((1500 - 700) * plan.numberOfTravelers * plan.numberOfDays).toDouble())} total.")

        if (!plan.needGuide)
            tips.add("📖 Free ASI audio guides and information boards are available at most sites.")

        if (plan.numberOfTravelers >= 4)
            tips.add("👥 Group bookings often get 10–15% discounts at hotels — ask at check-in!")

        if (tips.isEmpty())
            tips.add("✅ Your trip is well-optimised! You've made great choices.")

        return tips
    }

    // ── Smart recommendations ─────────────────────────────────────────────────

    private fun buildRecommendations(plan: TripPlan, perPerson: Double): List<String> {
        val recs = mutableListOf<String>()

        if (perPerson > 10000) {
            if (plan.hotelCategory != com.example.virasatnamma.data.model.HotelCategory.BUDGET)
                recs.add("💡 Switching to Standard hotels reduces cost by ~₹${formatINR(2000.0 * (plan.numberOfDays - 1))} per room.")
            if (plan.travelMode == TravelMode.CAB || plan.travelMode == TravelMode.FLIGHT)
                recs.add("💡 Consider KSRTC bus or train — significant savings on transport.")
        }

        if (plan.numberOfDays > 3)
            recs.add("💡 For ${plan.numberOfDays} days, booking hotels directly saves 10–15% vs. aggregators.")

        if (plan.numberOfTravelers >= 6)
            recs.add("💡 Renting a tempo traveller (₹3,000–4,500/day) beats multiple cabs for large groups.")

        recs.add("💡 Carry cash — many heritage site stalls, dhabas, and autos don't accept UPI.")
        recs.add("💡 Book accommodation at least 2 weeks in advance for peak season dates.")

        return recs
    }

    // ── Formatting helpers ────────────────────────────────────────────────────

    fun formatINR(amount: Double): String {
        val rounded = amount.roundToInt()
        return when {
            rounded >= 100000 -> "₹${String.format("%.1f", rounded / 100000.0)}L"
            rounded >= 1000   -> "₹${String.format("%,d", rounded)}"
            else              -> "₹$rounded"
        }
    }

    private fun Double.roundTo2() = (this * 100).roundToInt() / 100.0
}
