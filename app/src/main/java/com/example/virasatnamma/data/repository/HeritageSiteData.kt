package com.example.virasatnamma.data.repository

import com.example.virasatnamma.R
import com.example.virasatnamma.data.model.HeritageSite

object HeritageSiteData {

    val sites = listOf(
        HeritageSite(
            id = "site_001",
            name = "Hampi Virupaksha Temple",
            location = "Hampi, Ballari District",
            description = "The Virupaksha Temple is one of the most sacred temples in Karnataka, dedicated to Lord Shiva. Built in the 7th century, it is part of the Group of Monuments at Hampi, a UNESCO World Heritage Site. The temple's towering gopura (gateway tower) stands 50 metres tall and is visible from miles around. The temple complex contains smaller shrines, a large courtyard, and a sacred bathing ghāt on the banks of the Tungabhadra river. It has been in continuous worship since its construction, making it one of the oldest functional temples in India.",
            imageRes = R.drawable.site_hampi,
            audioRes = R.raw.audio_hampi
        ),
        HeritageSite(
            id = "site_002",
            name = "Mysore Palace",
            location = "Mysore, Mysuru District",
            description = "The Mysore Palace, also known as Amba Vilas Palace, is the official residence of the Wadiyar dynasty and the seat of the Kingdom of Mysore. Built between 1897 and 1912 in the Indo-Saracenic style, the palace is a masterpiece blending Hindu, Muslim, Rajput, and Gothic architectural styles. It is illuminated by nearly 100,000 light bulbs on Sundays and during the Dasara festival. The palace houses two durbar halls, numerous courtyards, twelve temples, and houses a large collection of historical artifacts and royal regalia.",
            imageRes = R.drawable.site_mysore,
            audioRes = R.raw.audio_mysore
        ),
        HeritageSite(
            id = "site_003",
            name = "Badami Cave Temples",
            location = "Badami, Bagalkot District",
            description = "The Badami Cave Temples are a complex of Hindu and Jain cave temples carved out of the sandstone cliffs of Badami. Dating from the 6th to 8th centuries CE, they were built during the Chalukya dynasty and represent some of the earliest examples of Chalukyan architecture. The four caves — three Hindu and one Jain — feature intricate carvings of deities, apsaras, and mythological scenes. Cave 3, the largest, features a magnificent sculpture of Vishnu in the Trivikrama form. The caves overlook the scenic Agastya Lake.",
            imageRes = R.drawable.site_badami,
            audioRes = R.raw.audio_badami
        ),
        HeritageSite(
            id = "site_004",
            name = "Gol Gumbaz",
            location = "Bijapur, Vijayapura District",
            description = "Gol Gumbaz is the mausoleum of Mohammed Adil Shah, Sultan of Bijapur, completed in 1656. It contains the second largest pre-modern dome ever built, outdone only by St Peter's Basilica in Rome. The dome, 44 metres in diameter, is supported by an interlocking system of eight intersecting arches. The famous 'Whispering Gallery' runs around the inside of the dome at a height of 33 metres — a whisper on one side can be heard clearly on the opposite side. The building is an outstanding example of Deccan Sultanate architecture.",
            imageRes = R.drawable.site_golgumbaz,
            audioRes = R.raw.audio_golgumbaz
        ),
        HeritageSite(
            id = "site_005",
            name = "Belur Chennakesava Temple",
            location = "Belur, Hassan District",
            description = "The Chennakeshava Temple at Belur is a 12th-century Hoysala architecture temple dedicated to Vishnu. Built by King Vishnuvardhana in 1117 CE to celebrate his victory over the Chola rulers, it took 103 years to complete. The temple stands on a star-shaped platform and is renowned for its intricate sculptures depicting scenes from Hindu mythology, the lives of Vishnu, and aspects of everyday life during the Hoysala period. The outer walls are covered with a continuous frieze of elephants, horses, scrolling foliage, and makaras, topped by rows of beautifully carved figures.",
            imageRes = R.drawable.site_belur,
            audioRes = R.raw.audio_belur
        )
    )

    fun findById(id: String): HeritageSite? = sites.find { it.id == id }
}
