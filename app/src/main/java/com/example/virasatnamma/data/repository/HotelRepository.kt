package com.example.virasatnamma.data.repository

import com.example.virasatnamma.data.model.Hotel

object HotelRepository {

    private val hotelsBySite: Map<String, List<Hotel>> = mapOf(

        // ── site_001: Hampi ────────────────────────────────────────────────
        "site_001" to listOf(
            Hotel("h001_1", "Hampi's Boulders Resort",   "Nagerhal, Hampi",       7500.0, 4.5, 4.2),
            Hotel("h001_2", "Royal Orchid Central",      "Hospet",                3200.0, 4.0, 13.0),
            Hotel("h001_3", "Kishkinda Heritage Resort", "Anegundi, Hampi",       4800.0, 4.3, 5.5),
            Hotel("h001_4", "Hotel Malligi",             "Hospet",                1800.0, 3.8, 12.5),
            Hotel("h001_5", "Hampi Waterfalls Resort",   "Gangavathi Road, Hampi",2500.0, 3.5, 8.0)
        ),

        // ── site_002: Mysore Palace ────────────────────────────────────────
        "site_002" to listOf(
            Hotel("h002_1", "Radisson Blu Plaza Hotel",  "Mysore",               7200.0, 4.6, 2.1),
            Hotel("h002_2", "Grand Mercure Mysore",      "Mysore",               6800.0, 4.5, 1.8),
            Hotel("h002_3", "Hotel Sandesh The Prince",  "Mysore",               4500.0, 4.2, 2.5),
            Hotel("h002_4", "Lalitha Mahal Palace Hotel","Mysore",              12000.0, 4.8, 3.5),
            Hotel("h002_5", "Hotel Mayura Hoysala",      "Mysore",               1800.0, 3.5, 1.2)
        ),

        // ── site_003: Badami Caves ─────────────────────────────────────────
        "site_003" to listOf(
            Hotel("h003_1", "Hotel Mayura Chalukya",     "Badami",               2200.0, 3.8, 1.5),
            Hotel("h003_2", "Badami Court Hotel",        "Badami",               3500.0, 4.1, 2.0),
            Hotel("h003_3", "Moustache Badami Hostel",   "Badami",                900.0, 4.2, 1.8),
            Hotel("h003_4", "Hotel Rajsangam",           "Bagalkot",             1500.0, 3.5, 30.0),
            Hotel("h003_5", "Jungle Lodges Badami",      "Badami",               4000.0, 4.0, 3.0)
        ),

        // ── site_004: Gol Gumbaz ───────────────────────────────────────────
        "site_004" to listOf(
            Hotel("h004_1", "Hotel Pearl",               "Vijayapura",           2800.0, 4.0, 1.5),
            Hotel("h004_2", "Hotel Madhuvan International","Vijayapura",         2200.0, 3.7, 2.0),
            Hotel("h004_3", "Hotel Tourist",             "Vijayapura",           1200.0, 3.2, 1.8),
            Hotel("h004_4", "Hotel Samrat",              "Vijayapura",           1500.0, 3.5, 2.5),
            Hotel("h004_5", "KSTDC Mayura Adil Shahi",   "Vijayapura",           1800.0, 3.6, 0.8)
        ),

        // ── site_005: Belur ────────────────────────────────────────────────
        "site_005" to listOf(
            Hotel("h005_1", "Hotel Mayura Velapuri",     "Belur",                1800.0, 3.7, 0.5),
            Hotel("h005_2", "Hoysala Village Resort",    "Hassan",               5500.0, 4.4, 28.0),
            Hotel("h005_3", "Hotel Hassan Ashok",        "Hassan",               2800.0, 3.9, 25.0),
            Hotel("h005_4", "Shalini Residency",         "Belur",                1200.0, 3.5, 1.0),
            Hotel("h005_5", "The Windflower Resort",     "Hassan",               6500.0, 4.6, 27.0)
        ),

        // ── site_006: Halebidu ─────────────────────────────────────────────
        "site_006" to listOf(
            Hotel("h006_1", "Hotel Mayura Shanthala",    "Halebidu",             1600.0, 3.6, 0.3),
            Hotel("h006_2", "Hoysala Village Resort",    "Hassan",               5500.0, 4.4, 16.0),
            Hotel("h006_3", "The Windflower Resort",     "Hassan",               6500.0, 4.6, 15.0),
            Hotel("h006_4", "Hotel Southern Star Hassan","Hassan",               3200.0, 4.0, 17.0),
            Hotel("h006_5", "Dew Drops Resort",          "Hassan",               4000.0, 4.1, 14.0)
        ),

        // ── site_007: Chitradurga Fort ─────────────────────────────────────
        "site_007" to listOf(
            Hotel("h007_1", "Hotel Aishwarya",           "Chitradurga",          1500.0, 3.5, 2.0),
            Hotel("h007_2", "Hotel Navodaya",            "Chitradurga",          1200.0, 3.3, 3.0),
            Hotel("h007_3", "Hotel Mayura Chitradurga",  "Chitradurga",          2000.0, 3.8, 1.5),
            Hotel("h007_4", "Sri Venkateswara Lodge",    "Chitradurga",           800.0, 3.0, 2.5),
            Hotel("h007_5", "Hotel Utsav",               "Chitradurga",          1800.0, 3.6, 1.8)
        ),

        // ── site_008: Srirangapatna ────────────────────────────────────────
        "site_008" to listOf(
            Hotel("h008_1", "Hotel Mayura River View",   "Srirangapatna",        2200.0, 3.8, 1.0),
            Hotel("h008_2", "Windmill Countryside Hotel","Mysore",               4500.0, 4.2, 18.0),
            Hotel("h008_3", "Hotel Coorg International", "Mysore",               3500.0, 4.0, 15.0),
            Hotel("h008_4", "Fort View Guesthouse",      "Srirangapatna",        1400.0, 3.5, 0.5),
            Hotel("h008_5", "The Elephant Court",        "Mysore",               8500.0, 4.7, 20.0)
        ),

        // ── site_009: Pattadakal ───────────────────────────────────────────
        "site_009" to listOf(
            Hotel("h009_1", "Hotel Mayura Chalukya",     "Badami",               2200.0, 3.8, 22.0),
            Hotel("h009_2", "Kamat Yatri Nivas",         "Pattadakal",           1500.0, 3.5, 1.0),
            Hotel("h009_3", "Badami Court Hotel",        "Badami",               3500.0, 4.1, 20.0),
            Hotel("h009_4", "Hotel Anand",               "Bagalkot",             1200.0, 3.2, 22.0),
            Hotel("h009_5", "Vatika Heritage Hotel",     "Pattadakal",           2800.0, 4.0, 0.8)
        ),

        // ── site_010: Aihole ───────────────────────────────────────────────
        "site_010" to listOf(
            Hotel("h010_1", "KSTDC Mayura Hotel",        "Aihole",               1800.0, 3.5, 0.5),
            Hotel("h010_2", "Aihole Tourist Guesthouse", "Aihole",               1000.0, 3.2, 0.3),
            Hotel("h010_3", "Hotel Mayura Chalukya",     "Badami",               2200.0, 3.8, 36.0),
            Hotel("h010_4", "Kamat Lodge",               "Badami",               1200.0, 3.3, 35.0),
            Hotel("h010_5", "Pattadakal Heritage Hotel", "Pattadakal",           2800.0, 4.0, 14.0)
        ),

        // ── site_011: Bangalore Palace ─────────────────────────────────────
        "site_011" to listOf(
            Hotel("h011_1", "Taj West End Bangalore",    "Race Course Rd, Bengaluru", 15000.0, 4.8, 2.5),
            Hotel("h011_2", "ITC Gardenia",              "Residency Rd, Bengaluru",   14000.0, 4.7, 3.0),
            Hotel("h011_3", "The Leela Palace Bengaluru","Airport Road, Bengaluru",   18000.0, 4.9, 5.0),
            Hotel("h011_4", "Hotel Chancery Pavilion",   "Residency Rd, Bengaluru",    6500.0, 4.3, 3.5),
            Hotel("h011_5", "Ibis Bengaluru City Centre","Bengaluru",                  3200.0, 4.0, 2.0)
        ),

        // ── site_012: Bidar Fort ───────────────────────────────────────────
        "site_012" to listOf(
            Hotel("h012_1", "Hotel Mayura Bidar",        "Bidar",                2000.0, 3.6, 1.5),
            Hotel("h012_2", "Hotel Ashoka",              "Bidar",                1500.0, 3.4, 2.0),
            Hotel("h012_3", "KSTDC Hotel",               "Bidar",                1800.0, 3.5, 1.8),
            Hotel("h012_4", "Hotel Nalanda",             "Bidar",                1200.0, 3.2, 2.5),
            Hotel("h012_5", "Bidar Heritage Guesthouse", "Bidar",                2500.0, 4.0, 0.8)
        )
    )

    // ── Primary method: site-specific ─────────────────────────────────────────
    fun getNearbyHotels(siteId: String): List<Hotel> =
        hotelsBySite[siteId] ?: emptyList()

    // ── Backward-compatible fallback ──────────────────────────────────────────
    fun getNearbyHotels(): List<Hotel> =
        hotelsBySite["site_001"] ?: emptyList()
}
