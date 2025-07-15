package com.fathi.electricitybillapp

import com.fathi.electricitybillapp.data.user.User
import com.fathi.electricitybillapp.data.user.UserDao
import com.fathi.electricitybillapp.data.user.UserEntity
import com.fathi.electricitybillapp.data.user.UserRepository
import com.fathi.electricitybillapp.data.user.UserRole
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.kotlin.whenever
import org.junit.jupiter.api.Test
import com.google.common.truth.Truth.assertThat
import org.mockito.junit.jupiter.MockitoExtension


@ExtendWith(MockitoExtension::class)
class UserRepositoryTest {

    @Mock
    private lateinit var userDao: UserDao

    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        userRepository = UserRepository(userDao)
    }

    @Test
    fun `given valid credentials when login then return user`() = runTest {
        // Given
        val username = "admin"
        val password = "admin123"
        val userEntity = UserEntity(
            id = 1,
            username = username,
            password = password,
            role = "ADMIN",
            customerId = null,
            name = "Administrator",
            email = "admin@electric.com",
            phone = "08123456789",
            address = "Kantor PLN",
            createdAt = System.currentTimeMillis()
        )

        whenever(userDao.login(username, password)).thenReturn(userEntity)

        // When
        val result = userRepository.login(username, password)

        // Then
        assertThat(result).isNotNull()
        assertThat(result!!.username).isEqualTo(username)
        assertThat(result.role).isEqualTo(UserRole.ADMIN)
        assertThat(result.name).isEqualTo("Administrator")
    }

    @Test
    fun `given invalid credentials when login then return null`() = runTest {
        // Given
        val username = "invalid"
        val password = "invalid"

        whenever(userDao.login(username, password)).thenReturn(null)

        // When
        val result = userRepository.login(username, password)

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `given user id when getUserById then return user`() = runTest {
        // Given
        val userId = 1
        val userEntity = UserEntity(
            id = userId,
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

        whenever(userDao.getUserById(userId)).thenReturn(userEntity)

        // When
        val result = userRepository.getUserById(userId)

        // Then
        assertThat(result).isNotNull()
        assertThat(result!!.id).isEqualTo(userId)
        assertThat(result.username).isEqualTo("admin")
    }

    @Test
    fun `given existing user when getAllCustomers then return customer list`() = runTest {
        // Given
        val customerEntity = UserEntity(
            id = 2,
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

        whenever(userDao.getAllCustomers()).thenReturn(listOf(customerEntity))

        // When
        val result = userRepository.getAllCustomers()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].role).isEqualTo(UserRole.CUSTOMER)
        assertThat(result[0].customerId).isEqualTo("CUST001")
    }

    @Test
    fun `given new user when insertUser then return user id`() = runTest {
        // Given
        val user = User(
            username = "newuser",
            password = "password123",
            role = UserRole.CUSTOMER,
            customerId = "CUST002",
            name = "New User",
            email = "newuser@example.com",
            phone = "08222222222",
            address = "Jl. Baru No. 456"
        )

        val expectedId = 3L
        whenever(userDao.insertUser(org.mockito.kotlin.any())).thenReturn(expectedId)

        // When
        val result = userRepository.insertUser(user)

        // Then
        assertThat(result).isEqualTo(expectedId)
    }
}