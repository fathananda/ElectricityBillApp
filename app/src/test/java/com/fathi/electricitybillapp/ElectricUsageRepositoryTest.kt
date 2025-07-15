package com.fathi.electricitybillapp

import com.fathi.electricitybillapp.data.bill.BillDao
import com.fathi.electricitybillapp.data.usage.ElectricUsage
import com.fathi.electricitybillapp.data.usage.ElectricUsageDao
import com.fathi.electricitybillapp.data.usage.ElectricUsageEntity
import com.fathi.electricitybillapp.data.usage.ElectricUsageRepository
import com.google.common.truth.Truth
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class ElectricUsageRepositoryTest {

    @Mock
    private lateinit var electricUsageDao: ElectricUsageDao

    @Mock
    private lateinit var billDao: BillDao

    private lateinit var electricUsageRepository: ElectricUsageRepository

    @BeforeEach
    fun setUp() {
        electricUsageRepository = ElectricUsageRepository(electricUsageDao, billDao)
    }

    @Test
    fun `given customer id when getUsageByCustomer then return usage list`() = runTest {
        // Given
        val customerId = "CUST001"
        val usageEntity = ElectricUsageEntity(
            id = 1,
            customerId = customerId,
            month = 11,
            year = 2024,
            previousReading = 1000.0,
            currentReading = 1150.0,
            usageKwh = 150.0,
            ratePerKwh = 1467.28,
            totalAmount = 220092.0,
            isPaid = false,
            dueDate = System.currentTimeMillis(),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        whenever(electricUsageDao.getUsageByCustomer(customerId)).thenReturn(listOf(usageEntity))

        val result = electricUsageRepository.getUsageByCustomer(customerId)

        Truth.assertThat(result).hasSize(1)
        Truth.assertThat(result[0].customerId).isEqualTo(customerId)
        Truth.assertThat(result[0].usageKwh).isEqualTo(150.0)
        Truth.assertThat(result[0].totalAmount).isEqualTo(220092.0)
    }

    @Test
    fun `given electric usage when insertUsage then auto generate bill`() = runTest {
        val usage = ElectricUsage(
            customerId = "CUST001",
            month = 11,
            year = 2024,
            previousReading = 1000.0,
            currentReading = 1150.0,
            usageKwh = 150.0,
            ratePerKwh = 1467.28,
            totalAmount = 220092.0,
            isPaid = false,
            dueDate = System.currentTimeMillis()
        )

        val usageId = 1L
        whenever(electricUsageDao.insertUsage(any())).thenReturn(usageId)
        whenever(billDao.insertBill(any())).thenReturn(1L)

        val result = electricUsageRepository.insertUsage(usage)

        Truth.assertThat(result).isEqualTo(usageId)
        verify(electricUsageDao).insertUsage(any())
        verify(billDao).insertBill(any())
    }
}