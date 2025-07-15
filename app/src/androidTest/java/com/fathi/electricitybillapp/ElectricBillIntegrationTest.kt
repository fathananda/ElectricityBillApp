package com.fathi.electricitybillapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fathi.electricitybillapp.app.ElectricBillDatabase
import com.fathi.electricitybillapp.data.bill.BillRepository
import com.fathi.electricitybillapp.data.usage.ElectricUsage
import com.fathi.electricitybillapp.data.usage.ElectricUsageRepository
import com.fathi.electricitybillapp.data.user.User
import com.fathi.electricitybillapp.data.user.UserRepository
import com.fathi.electricitybillapp.data.user.UserRole
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import java.util.Calendar
import androidx.arch.core.executor.testing.InstantTaskExecutorRule


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ElectricBillIntegrationTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ElectricBillDatabase
    private lateinit var userRepository: UserRepository
    private lateinit var usageRepository: ElectricUsageRepository
    private lateinit var billRepository: BillRepository

    @Before
    fun setUp() {
        // Setup in-memory database
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ElectricBillDatabase::class.java
        ).allowMainThreadQueries().build()

        // Initialize repositories
        userRepository = UserRepository(database.userDao())
        usageRepository = ElectricUsageRepository(database.electricUsageDao(), database.billDao())
        billRepository = BillRepository(database.billDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    // 1. Test Integrasi User Authentication & Data Persistence
    @Test
    fun testUserAuthenticationIntegration() = runTest {
        // Arrange - Persiapan data uji
        val testUser = User(
            username = "testuser",
            password = "testpass123",
            role = UserRole.CUSTOMER,
            customerId = "CUST001",
            name = "Test User",
            email = "test@example.com",
            phone = "081234567890",
            address = "Test Address"
        )

        // Act - Simpan user dan coba login
        userRepository.insertUser(testUser)
        val loginResult = userRepository.login("testuser", "testpass123")

        // Assert - Verifikasi integrasi berhasil
        assertNotNull("Login should succeed", loginResult)
        assertEquals("Username should match", "testuser", loginResult?.username)
        assertEquals("Role should match", UserRole.CUSTOMER, loginResult?.role)
        assertEquals("Customer ID should match", "CUST001", loginResult?.customerId)
    }

    // 2. Test Integrasi Usage Creation & Bill Auto-Generation
    @Test
    fun testUsageCreationAndBillGenerationIntegration() = runTest {
        // Arrange - Setup user dan data usage
        val customer = User(
            username = "customer1",
            password = "pass123",
            role = UserRole.CUSTOMER,
            customerId = "CUST001",
            name = "Customer One",
            email = "customer1@example.com",
            phone = "081111111111",
            address = "Address 1"
        )
        userRepository.insertUser(customer)

        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)
        val dueDate = System.currentTimeMillis() + (30 * 24 * 60 * 60 * 1000L)

        val electricUsage = ElectricUsage(
            customerId = "CUST001",
            month = currentMonth,
            year = currentYear,
            previousReading = 1000.0,
            currentReading = 1200.0,
            usageKwh = 200.0,
            ratePerKwh = 1467.28,
            totalAmount = 200.0 * 1467.28,
            dueDate = dueDate
        )

        // Act - Insert usage (should auto-generate bill)
        val usageId = usageRepository.insertUsage(electricUsage)

        // Assert - Verifikasi usage tersimpan
        val savedUsage = usageRepository.getUsageByCustomer("CUST001")
        assertFalse("Usage should be saved", savedUsage.isEmpty())
        assertEquals("Usage amount should match", 200.0, savedUsage[0].usageKwh, 0.01)

        // Assert - Verifikasi bill otomatis terbuat
        val bills = billRepository.getBillsByCustomer("CUST001")
        assertFalse("Bill should be auto-generated", bills.isEmpty())
        assertEquals("Bill month should match", currentMonth, bills[0].month)
        assertEquals("Bill year should match", currentYear, bills[0].year)
        assertEquals("Bill amount should match usage", savedUsage[0].totalAmount, bills[0].amount, 0.01)
        assertEquals("Total amount should include admin fee",
            savedUsage[0].totalAmount + 2500.0, bills[0].totalAmount, 0.01)
    }

    // 3. Test Integrasi Bill Payment Process
    @Test
    fun testBillPaymentIntegration() = runTest {
        // Arrange - Setup customer, usage, dan bill
        val customer = User(
            username = "customer2",
            password = "pass123",
            role = UserRole.CUSTOMER,
            customerId = "CUST002",
            name = "Customer Two",
            email = "customer2@example.com",
            phone = "082222222222",
            address = "Address 2"
        )
        userRepository.insertUser(customer)

        val usage = ElectricUsage(
            customerId = "CUST002",
            month = 11,
            year = 2024,
            previousReading = 500.0,
            currentReading = 650.0,
            usageKwh = 150.0,
            ratePerKwh = 1467.28,
            totalAmount = 150.0 * 1467.28,
            dueDate = System.currentTimeMillis() + (30 * 24 * 60 * 60 * 1000L)
        )
        usageRepository.insertUsage(usage)

        // Get the generated bill
        val bills = billRepository.getBillsByCustomer("CUST002")
        val unpaidBill = bills.first { !it.isPaid }

        // Act - Process payment
        val beforePayment = System.currentTimeMillis()
        val paidBill = unpaidBill.copy(
            isPaid = true,
            paidAt = System.currentTimeMillis()
        )
        billRepository.updateBill(paidBill)

        // Assert - Verifikasi pembayaran berhasil
        val updatedBills = billRepository.getBillsByCustomer("CUST002")
        val paymentResult = updatedBills.find { it.id == unpaidBill.id }

        assertNotNull("Bill should exist after payment", paymentResult)
        assertTrue("Bill should be marked as paid", paymentResult!!.isPaid)
        assertNotNull("Payment date should be recorded", paymentResult.paidAt)
        assertTrue("Payment date should be recent", paymentResult.paidAt!! >= beforePayment)
    }

    // 4. Test Integrasi Data Validation & Error Handling
    @Test
    fun testDataValidationIntegration() = runTest {
        // Arrange - Setup customer
        val customer = User(
            username = "customer3",
            password = "pass123",
            role = UserRole.CUSTOMER,
            customerId = "CUST003",
            name = "Customer Three",
            email = "customer3@example.com",
            phone = "083333333333",
            address = "Address 3"
        )
        userRepository.insertUser(customer)

        // Test Case 1: Duplikasi usage untuk bulan yang sama
        val usage1 = ElectricUsage(
            customerId = "CUST003",
            month = 12,
            year = 2024,
            previousReading = 100.0,
            currentReading = 200.0,
            usageKwh = 100.0,
            ratePerKwh = 1467.28,
            totalAmount = 100.0 * 1467.28,
            dueDate = System.currentTimeMillis() + (30 * 24 * 60 * 60 * 1000L)
        )
        usageRepository.insertUsage(usage1)

        // Act - Coba insert usage duplikat
        val existingUsage = usageRepository.getUsageByMonthYear("CUST003", 12, 2024)

        // Assert - Verifikasi validasi duplikasi
        assertNotNull("Usage should exist for the month", existingUsage)
        assertEquals("Usage month should match", 12, existingUsage?.month)
        assertEquals("Usage year should match", 2024, existingUsage?.year)
    }

    // 5. Test Integrasi Multi-User Data Isolation
    @Test
    fun testMultiUserDataIsolation() = runTest {
        // Arrange - Setup multiple customers
        val customer1 = User(
            username = "customer4",
            password = "pass123",
            role = UserRole.CUSTOMER,
            customerId = "CUST004",
            name = "Customer Four",
            email = "customer4@example.com",
            phone = "084444444444",
            address = "Address 4"
        )

        val customer2 = User(
            username = "customer5",
            password = "pass123",
            role = UserRole.CUSTOMER,
            customerId = "CUST005",
            name = "Customer Five",
            email = "customer5@example.com",
            phone = "085555555555",
            address = "Address 5"
        )

        userRepository.insertUser(customer1)
        userRepository.insertUser(customer2)

        // Act - Create usage for both customers
        val usage1 = ElectricUsage(
            customerId = "CUST004",
            month = 10,
            year = 2024,
            previousReading = 200.0,
            currentReading = 300.0,
            usageKwh = 100.0,
            ratePerKwh = 1467.28,
            totalAmount = 100.0 * 1467.28,
            dueDate = System.currentTimeMillis() + (30 * 24 * 60 * 60 * 1000L)
        )

        val usage2 = ElectricUsage(
            customerId = "CUST005",
            month = 10,
            year = 2024,
            previousReading = 300.0,
            currentReading = 450.0,
            usageKwh = 150.0,
            ratePerKwh = 1467.28,
            totalAmount = 150.0 * 1467.28,
            dueDate = System.currentTimeMillis() + (30 * 24 * 60 * 60 * 1000L)
        )

        usageRepository.insertUsage(usage1)
        usageRepository.insertUsage(usage2)

        // Assert - Verifikasi data isolation
        val customer1Usages = usageRepository.getUsageByCustomer("CUST004")
        val customer2Usages = usageRepository.getUsageByCustomer("CUST005")

        assertEquals("Customer 1 should have 1 usage", 1, customer1Usages.size)
        assertEquals("Customer 2 should have 1 usage", 1, customer2Usages.size)
        assertEquals("Customer 1 usage amount should be 100", 100.0, customer1Usages[0].usageKwh, 0.01)
        assertEquals("Customer 2 usage amount should be 150", 150.0, customer2Usages[0].usageKwh, 0.01)

        val customer1Bills = billRepository.getBillsByCustomer("CUST004")
        val customer2Bills = billRepository.getBillsByCustomer("CUST005")

        assertEquals("Customer 1 should have 1 bill", 1, customer1Bills.size)
        assertEquals("Customer 2 should have 1 bill", 1, customer2Bills.size)
        assertNotEquals("Bills should have different amounts",
            customer1Bills[0].totalAmount, customer2Bills[0].totalAmount)
    }

    // 6. Test Integrasi Admin Access Control
    @Test
    fun testAdminAccessControlIntegration() = runTest {
        // Arrange - Setup admin user
        val admin = User(
            username = "admin",
            password = "admin123",
            role = UserRole.ADMIN,
            customerId = null,
            name = "Administrator",
            email = "admin@electric.com",
            phone = "08123456789",
            address = "PLN Office"
        )
        userRepository.insertUser(admin)

        // Setup customers dengan data
        val customer = User(
            username = "customer6",
            password = "pass123",
            role = UserRole.CUSTOMER,
            customerId = "CUST006",
            name = "Customer Six",
            email = "customer6@example.com",
            phone = "086666666666",
            address = "Address 6"
        )
        userRepository.insertUser(customer)

        val usage = ElectricUsage(
            customerId = "CUST006",
            month = 9,
            year = 2024,
            previousReading = 400.0,
            currentReading = 520.0,
            usageKwh = 120.0,
            ratePerKwh = 1467.28,
            totalAmount = 120.0 * 1467.28,
            dueDate = System.currentTimeMillis() + (30 * 24 * 60 * 60 * 1000L)
        )
        usageRepository.insertUsage(usage)

        // Act - Admin access all data
        val allCustomers = userRepository.getAllCustomers()
        val allUsages = usageRepository.getAllUsages()
        val allBills = billRepository.getAllBills()

        // Assert - Verifikasi admin dapat mengakses semua data
        assertFalse("Admin should see all customers", allCustomers.isEmpty())
        assertFalse("Admin should see all usages", allUsages.isEmpty())
        assertFalse("Admin should see all bills", allBills.isEmpty())

        val adminUser = userRepository.login("admin", "admin123")
        assertNotNull("Admin should be able to login", adminUser)
        assertEquals("Admin role should be correct", UserRole.ADMIN, adminUser?.role)
        assertNull("Admin should not have customer ID", adminUser?.customerId)
    }
}