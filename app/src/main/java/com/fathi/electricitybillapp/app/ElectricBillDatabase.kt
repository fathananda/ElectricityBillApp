package com.fathi.electricitybillapp.app

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.fathi.electricitybillapp.data.bill.BillDao
import com.fathi.electricitybillapp.data.bill.BillEntity
import com.fathi.electricitybillapp.data.usage.ElectricUsageDao
import com.fathi.electricitybillapp.data.usage.ElectricUsageEntity
import com.fathi.electricitybillapp.data.user.UserDao
import com.fathi.electricitybillapp.data.user.UserEntity
import java.util.Date

/**
 * Database Room untuk aplikasi pembayaran listrik
 *
 * Kegunaan:
 * - Menyediakan akses ke semua DAO (Data Access Object)
 * - Konfigurasi database dengan TypeConverter
 * - Mengelola entitas User, ElectricUsage, dan Bill
 *
 * @author Fathi
 * @since 2025
 */

@Database(
    entities = [UserEntity::class, ElectricUsageEntity::class, BillEntity::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class ElectricBillDatabase : RoomDatabase(){
    abstract fun userDao(): UserDao
    abstract fun electricUsageDao(): ElectricUsageDao
    abstract fun billDao(): BillDao
}

class Converters{
    @TypeConverter
    fun  fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }

    }
}