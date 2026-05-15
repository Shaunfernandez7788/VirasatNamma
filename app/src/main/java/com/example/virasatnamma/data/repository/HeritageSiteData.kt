package com.example.virasatnamma.data.repository

import com.example.virasatnamma.R
import com.example.virasatnamma.data.model.HeritageSite

object HeritageSiteData {

    val sites = listOf(

        // ── 1. Hampi ───────────────────────────────────────────────────────
        HeritageSite(
            id = "site_001",
            name = "Hampi Virupaksha Temple",
            location = "Hampi, Ballari District",
            description = "The Virupaksha Temple is one of the most sacred temples in Karnataka, dedicated to Lord Shiva. " +
                    "Built in the 7th century, it is part of the Group of Monuments at Hampi, a UNESCO World Heritage Site. " +
                    "The temple's towering gopura stands 50 metres tall and is visible from miles around. " +
                    "The temple complex contains smaller shrines, a large courtyard, and a sacred bathing ghāt on the " +
                    "banks of the Tungabhadra river. It has been in continuous worship since its construction, making it " +
                    "one of the oldest functional temples in India. The site once served as the capital of the mighty " +
                    "Vijayanagara Empire, which at its peak was one of the largest and wealthiest empires in the world.",
            imageRes = R.drawable.site_hampi,
            audioRes = R.raw.audio_hampi,
            distance = 340.0
        ),

        // ── 2. Mysore Palace ───────────────────────────────────────────────
        HeritageSite(
            id = "site_002",
            name = "Mysore Palace",
            location = "Mysore, Mysuru District",
            description = "The Mysore Palace, also known as Amba Vilas Palace, is the official residence of the Wadiyar " +
                    "dynasty and the seat of the Kingdom of Mysore. Built between 1897 and 1912 in the Indo-Saracenic " +
                    "style, the palace is a masterpiece blending Hindu, Muslim, Rajput, and Gothic architectural styles. " +
                    "It is illuminated by nearly 100,000 light bulbs on Sundays and during the Dasara festival. " +
                    "The palace houses two durbar halls, numerous courtyards, twelve temples, and a large collection of " +
                    "historical artifacts and royal regalia. It is one of the most visited tourist sites in India, " +
                    "attracting over 6 million visitors annually.",
            imageRes = R.drawable.site_mysore,
            audioRes = R.raw.audio_mysore,
            distance = 143.0
        ),

        // ── 3. Badami Caves ────────────────────────────────────────────────
        HeritageSite(
            id = "site_003",
            name = "Badami Cave Temples",
            location = "Badami, Bagalkot District",
            description = "The Badami Cave Temples are a complex of Hindu and Jain cave temples carved out of the " +
                    "sandstone cliffs of Badami, dating from the 6th to 8th centuries CE during the Chalukya dynasty. " +
                    "They represent some of the earliest examples of Chalukyan architecture. " +
                    "The four caves — three Hindu and one Jain — feature intricate carvings of deities, apsaras, " +
                    "and mythological scenes. Cave 3, the largest, features a magnificent sculpture of Vishnu in the " +
                    "Trivikrama form spanning nearly the entire cave wall. The caves overlook the scenic Agastya Lake, " +
                    "surrounded by red sandstone hills that glow golden at sunset.",
            imageRes = R.drawable.site_badami,
            audioRes = R.raw.audio_badami,
            distance = 480.0
        ),

        // ── 4. Gol Gumbaz ──────────────────────────────────────────────────
        HeritageSite(
            id = "site_004",
            name = "Gol Gumbaz",
            location = "Bijapur, Vijayapura District",
            description = "Gol Gumbaz is the mausoleum of Mohammed Adil Shah, Sultan of Bijapur, completed in 1656. " +
                    "It contains the second largest pre-modern dome ever built, outdone only by St Peter's Basilica in Rome. " +
                    "The dome is 44 metres in diameter and supported by an interlocking system of eight intersecting arches. " +
                    "The famous 'Whispering Gallery' runs around the inside of the dome at a height of 33 metres — " +
                    "a whisper on one side can be heard clearly on the opposite side. " +
                    "The building is an outstanding example of Deccan Sultanate architecture and is surrounded by a " +
                    "spacious garden with a small archaeological museum.",
            imageRes = R.drawable.site_golgumbaz,
            audioRes = R.raw.audio_golgumbaz,
            distance = 520.0
        ),

        // ── 5. Belur ───────────────────────────────────────────────────────
        HeritageSite(
            id = "site_005",
            name = "Belur Chennakesava Temple",
            location = "Belur, Hassan District",
            description = "The Chennakeshava Temple at Belur is a 12th-century Hoysala architecture temple dedicated to Vishnu. " +
                    "Built by King Vishnuvardhana in 1117 CE to celebrate his victory over the Chola rulers, " +
                    "it took 103 years to complete. The temple stands on a star-shaped platform and is renowned for " +
                    "its intricate sculptures depicting scenes from Hindu mythology, the lives of Vishnu, and aspects of " +
                    "everyday life during the Hoysala period. The outer walls are covered with a continuous frieze of " +
                    "elephants, horses, scrolling foliage, and makaras topped by rows of beautifully carved figures. " +
                    "The temple is still an active place of worship.",
            imageRes = R.drawable.site_belur,
            audioRes = R.raw.audio_belur,
            distance = 222.0
        ),

        // ── 6. Halebidu ────────────────────────────────────────────────────
        HeritageSite(
            id = "site_006",
            name = "Halebidu Hoysaleswara Temple",
            location = "Halebidu, Hassan District",
            description = "The Hoysaleswara Temple at Halebidu is one of the finest examples of Hoysala architecture, " +
                    "built in the 12th century CE under the patronage of King Vishnuvardhana. " +
                    "The twin temples dedicated to Lord Shiva are renowned for their breathtakingly detailed carvings " +
                    "covering the entire exterior. The sculptural narrative includes scenes from the Ramayana, " +
                    "Mahabharata, Bhagavata Purana, and depictions of the Hoysala emblem — a warrior slaying a tiger. " +
                    "The temple was never fully completed yet stands as one of Karnataka's greatest architectural " +
                    "achievements. The site is maintained by the Archaeological Survey of India.",
            imageRes = R.drawable.site_halebidu,
            audioRes = R.raw.audio_halebidu,
            distance = 210.0
        ),

        // ── 7. Chitradurga Fort ────────────────────────────────────────────
        HeritageSite(
            id = "site_007",
            name = "Chitradurga Fort",
            location = "Chitradurga, Chitradurga District",
            description = "Chitradurga Fort, also known as the 'Fort of Seven Rounds' or Kallina Kote, is a hill fortress " +
                    "spread across several granite boulders in central Karnataka. " +
                    "Built between the 10th and 18th centuries by the Nayakas of Chitradurga and later expanded by " +
                    "Hyder Ali and Tipu Sultan, the fort contains 19 temples, 4 secret entrances, 38 rear entrances, " +
                    "and numerous water tanks carved out of rock. The legendary story of Onake Obavva — a woman warrior " +
                    "who defended the fort single-handedly against the armies of Hyder Ali using only a pestle — " +
                    "is celebrated here as a symbol of courage and patriotism.",
            imageRes = R.drawable.site_chitradurga,
            audioRes = R.raw.audio_chitradurga,
            distance = 200.0
        ),

        // ── 8. Srirangapatna ───────────────────────────────────────────────
        HeritageSite(
            id = "site_008",
            name = "Srirangapatna Fort & Dariya Daulat",
            location = "Srirangapatna, Mandya District",
            description = "Srirangapatna is a historic island town in the Cauvery river, famous as the capital of Hyder Ali " +
                    "and Tipu Sultan — the Tiger of Mysore. The island fort houses the Ranganathaswamy Temple, " +
                    "the Daria Daulat Bagh (Tipu's summer palace), the Gumbaz mausoleum containing the tombs of " +
                    "Hyder Ali and Tipu Sultan, and the ruins of the fort walls. " +
                    "The Daria Daulat Bagh is a masterpiece of Indo-Saracenic art with stunning murals depicting the " +
                    "Battle of Pollilur. The island served as the de facto capital of the Kingdom of Mysore during " +
                    "Tipu Sultan's reign from 1782 to 1799.",
            imageRes = R.drawable.site_srirangapatna,
            audioRes = R.raw.audio_srirangapatna,
            distance = 128.0
        ),

        // ── 9. Pattadakal ──────────────────────────────────────────────────
        HeritageSite(
            id = "site_009",
            name = "Pattadakal Temples",
            location = "Pattadakal, Bagalkot District",
            description = "Pattadakal is a UNESCO World Heritage Site featuring a group of 8th century CE Hindu and Jain " +
                    "temples built during the Chalukya dynasty. The site represents a high point in early Chalukyan " +
                    "architecture, synthesising the Nagara (North Indian) and Dravidian (South Indian) architectural styles. " +
                    "The Virupaksha Temple, built in 745 CE, is the largest and most elaborate — constructed by Queen " +
                    "Lokamahadevi to commemorate her husband King Vikramaditya II's victory over the Pallavas. " +
                    "The site contains 10 major temples in all and is located on the banks of the Malaprabha river, " +
                    "just 22 km from Badami.",
            imageRes = R.drawable.site_pattadakal,
            audioRes = R.raw.audio_pattadakal,
            distance = 490.0
        ),

        // ── 10. Aihole ─────────────────────────────────────────────────────
        HeritageSite(
            id = "site_010",
            name = "Aihole — Cradle of Indian Architecture",
            location = "Aihole, Bagalkot District",
            description = "Aihole, often called the 'Cradle of Indian Temple Architecture', is a site of over 125 temples " +
                    "built between the 4th and 12th centuries CE, representing the earliest experiments in Dravidian " +
                    "temple architecture by the Chalukya dynasty. The Durga Temple is the most famous, built in the " +
                    "7th–8th century on a raised platform with an apsidal (semi-circular) plan unique in Indian temple " +
                    "architecture. The Lad Khan Temple, built c. 450 CE, is one of the oldest surviving Hindu temples in India. " +
                    "Aihole served as the architectural laboratory where ancient Indian builders experimented with temple " +
                    "forms that would define South Indian architecture for centuries.",
            imageRes = R.drawable.site_aihole,
            audioRes = R.raw.audio_aihole,
            distance = 484.0
        ),

        // ── 11. Bangalore Palace ───────────────────────────────────────────
        HeritageSite(
            id = "site_011",
            name = "Bangalore Palace",
            location = "Vasanth Nagar, Bengaluru",
            description = "Bangalore Palace was built in 1887 and inspired by England's Windsor Castle. " +
                    "Purchased by the Mysore royal family in 1884, it became the urban seat of the Wodeyar dynasty. " +
                    "The palace is built in the Tudor Revival style featuring fortified towers, arched colonnades, " +
                    "green lawns, and a central courtyard. The interiors are richly decorated with elegant woodwork, " +
                    "floral motifs, Venetian chandeliers, and cornices. The walls are adorned with paintings, " +
                    "photographs, and mounted hunting trophies. " +
                    "It serves as the venue for the iconic Bangalore Palace Grounds concerts and annual exhibitions " +
                    "that draw millions of visitors every year.",
            imageRes = R.drawable.site_bangalore_palace,
            audioRes = R.raw.audio_bangalore,
            distance = 5.0
        ),

        // ── 12. Bidar Fort ─────────────────────────────────────────────────
        HeritageSite(
            id = "site_012",
            name = "Bidar Fort",
            location = "Bidar, Bidar District",
            description = "Bidar Fort is one of the most magnificent medieval forts in the Deccan, built in the 15th century " +
                    "by Ahmad Shah Wali Bahmani, founder of the Bahmani Sultanate. " +
                    "The fort features an impressive triple moat system, massive ramparts, and 37 bastions, making it " +
                    "virtually impregnable in its heyday. Inside the complex are the ruins of the Royal Palace " +
                    "(Rangeen Mahal) decorated with stunning mother-of-pearl inlay work, the Solah Khamba Mosque with " +
                    "16 columns, and the Tarkash Mahal. " +
                    "Bidar is also famous for its unique Bidriware metal craft — a 600-year-old tradition of inlaying " +
                    "silver on a zinc-copper alloy that originated in this very city.",
            imageRes = R.drawable.site_bidar,
            audioRes = R.raw.audio_bidar,
            distance = 690.0
        )
    )

    fun findById(id: String): HeritageSite? = sites.find { it.id == id }
}
