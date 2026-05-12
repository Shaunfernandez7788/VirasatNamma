package com.example.virasatnamma.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "check_ins")
data class CheckInEntity(
    @PrimaryKey
    val siteId: String,
    val siteName: String,
    val siteLocation: String,
    val checkedInAt: Long = System.currentTimeMillis()
)
