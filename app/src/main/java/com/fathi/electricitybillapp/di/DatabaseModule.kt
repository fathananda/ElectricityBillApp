package com.fathi.electricitybillapp.di

import android.content.Context
import androidx.room.Room
import com.fathi.electricitybillapp.app.ElectricBillDatabase
import com.fathi.electricitybillapp.data.bill.BillDao
import com.fathi.electricitybillapp.data.usage.ElectricUsageDao
import com.fathi.electricitybillapp.data.user.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ElectricBillDatabase {
        return Room.databaseBuilder(
            context,
            ElectricBillDatabase::class.java,
            "electric_bill_database"
        ).build()
    }

    @Provides
    fun provideUserDao(database: ElectricBillDatabase): UserDao = database.userDao()

    @Provides
    fun provideElectricUsageDao(database: ElectricBillDatabase): ElectricUsageDao = database.electricUsageDao()

    @Provides
    fun provideBillDao(database: ElectricBillDatabase): BillDao = database.billDao()
}