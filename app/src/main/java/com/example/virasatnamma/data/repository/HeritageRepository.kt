package com.example.virasatnamma.data.repository

import com.example.virasatnamma.data.local.CheckInDao
import com.example.virasatnamma.data.local.CheckInEntity
import com.example.virasatnamma.data.model.HeritageSite
import kotlinx.coroutines.flow.Flow

class HeritageRepository(private val checkInDao: CheckInDao) {

    fun getAllSites(): List<HeritageSite> = HeritageSiteData.sites

    fun getSiteById(id: String): HeritageSite? =
        HeritageSiteData.findById(id)

    fun getAllCheckIns(): Flow<List<CheckInEntity>> =
        checkInDao.getAllCheckIns()

    suspend fun checkIn(site: HeritageSite) {
        checkInDao.insertCheckIn(
            CheckInEntity(
                siteId = site.id,
                siteName = site.name,
                siteLocation = site.location
            )
        )
    }

    suspend fun isCheckedIn(siteId: String): Boolean =
        checkInDao.isCheckedIn(siteId)

    // ✅ EDIT feature
    suspend fun updateCheckIn(checkIn: CheckInEntity) {
        checkInDao.updateCheckIn(checkIn)
    }

    // ✅ DELETE feature
    suspend fun deleteCheckIn(checkIn: CheckInEntity) {
        checkInDao.deleteCheckIn(checkIn)
    }
}