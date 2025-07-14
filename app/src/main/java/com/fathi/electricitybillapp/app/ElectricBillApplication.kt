package com.fathi.electricitybillapp.app

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.fathi.electricitybillapp.data.bill.BillEntity
import com.fathi.electricitybillapp.data.usage.ElectricUsageEntity
import com.fathi.electricitybillapp.data.user.UserEntity
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class ElectricBillApplication : Application(){
    val dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate() {
        super.onCreate()
        // Initialize default users
        CoroutineScope(Dispatchers.IO).launch {
            initializeDefaultUsers()
        }
    }

    private suspend fun initializeDefaultUsers() {
        val database = Room.databaseBuilder(
            this,
            ElectricBillDatabase::class.java,
            "electric_bill_database"
        ).build()

        val userDao = database.userDao()

        // Check if admin exists
        val adminExists = userDao.login("admin", "admin123")
        if (adminExists == null) {
            val admin = UserEntity(
                username = "admin",
                password = "admin123",
                role = "ADMIN",
                customerId = null,
                name = "Administrator",
                email = "admin@electric.com",
                phone = "08123456789",
                address = "Kantor PLN",
                createdAt = System.currentTimeMillis()
            )
            userDao.insertUser(admin)
        }

        // Check if customer exists
        val customerExists = userDao.login("customer", "customer123")
        if (customerExists == null) {
            val customer = UserEntity(
                username = "customer",
                password = "customer123",
                role = "CUSTOMER",
                customerId = "CUST001",
                name = "John Doe",
                email = "john@example.com",
                phone = "08111111111",
                address = "Jl. Contoh No. 123",
                createdAt = System.currentTimeMillis()
            )
            userDao.insertUser(customer)

            // Add sample electric usage
            val usageDao = database.electricUsageDao()
            val sampleUsage = ElectricUsageEntity(
                customerId = "CUST001",
                month = 11,
                year = 2024,
                previousReading = 1000.0,
                currentReading = 1150.0,
                usageKwh = 150.0,
                ratePerKwh = 1467.28,
                totalAmount = 150.0 * 1467.28,
                isPaid = false,
                dueDate = System.currentTimeMillis() + (30 * 24 * 60 * 60 * 1000L),
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            val usageId = usageDao.insertUsage(sampleUsage)

            // Add sample bill
            val billDao = database.billDao()
            val sampleBill = BillEntity(
                customerId = "CUST001",
                usageId = usageId.toInt(),
                month = 11,
                year = 2024,
                usageKwh = 150.0,
                amount = 150.0 * 1467.28,
                adminFee = 2500.0,
                totalAmount = (150.0 * 1467.28) + 2500.0,
                isPaid = false,
                paidAt = null,
                dueDate = System.currentTimeMillis() + (30 * 24 * 60 * 60 * 1000L),
                createdAt = System.currentTimeMillis()
            )
            billDao.insertBill(sampleBill)
        }
    }
}