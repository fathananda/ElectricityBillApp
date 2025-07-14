package com.fathi.electricitybillapp.data.usage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fathi.electricitybillapp.data.usage.ElectricUsageEntity

@Dao
interface ElectricUsageDao {
    @Query("SELECT * FROM electric_usage WHERE customerId = :customerId ORDER BY year DESC, month DESC")
    suspend fun getUsageByCustomer(customerId: String): List<ElectricUsageEntity>

    @Query("SELECT * FROM electric_usage WHERE id = :id")
    suspend fun getUsageById(id: Int): ElectricUsageEntity?

    @Query("SELECT * FROM electric_usage ORDER BY year DESC, month DESC")
    suspend fun getAllUsages(): List<ElectricUsageEntity>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertUsage(usage: ElectricUsageEntity): Long

    @Update
    suspend fun updateUsage(usage: ElectricUsageEntity)

    @Delete
    suspend fun deleteUsage(usage: ElectricUsageEntity)

    @Query("SELECT * FROM electric_usage WHERE customerId = :customerId AND month = :month AND year = :year")
    suspend fun getUsageByMonthYear(customerId: String, month: Int, year: Int): ElectricUsageEntity?
}