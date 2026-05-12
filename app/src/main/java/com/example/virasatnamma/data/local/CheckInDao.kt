package com.example.virasatnamma.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckInDao {

    @Query("SELECT * FROM check_ins ORDER BY checkedInAt DESC")
    fun getAllCheckIns(): Flow<List<CheckInEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckIn(checkIn: CheckInEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM check_ins WHERE siteId = :siteId)")
    suspend fun isCheckedIn(siteId: String): Boolean

    // ✅ EDIT feature
    @Update
    suspend fun updateCheckIn(checkIn: CheckInEntity)

    // ✅ DELETE feature
    @Delete
    suspend fun deleteCheckIn(checkIn: CheckInEntity)
}