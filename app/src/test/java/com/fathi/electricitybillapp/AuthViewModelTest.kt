package com.fathi.electricitybillapp

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.fathi.electricitybillapp.data.user.User
import com.fathi.electricitybillapp.data.user.UserRepository
import com.fathi.electricitybillapp.data.user.UserRole
import com.fathi.electricitybillapp.util.PreferencesKeys
import com.fathi.electricitybillapp.viewmodel.auth.AuthViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import org.mockito.kotlin.any


@ExtendWith(MockitoExtension::class)
class AuthViewModelTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var dataStore: DataStore<Preferences>

    @Mock
    private lateinit var preferences: Preferences

    private lateinit var authViewModel: AuthViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        whenever(dataStore.data).thenReturn(flowOf(preferences))
        whenever(preferences[PreferencesKeys.USER_ID]).thenReturn(null)

        authViewModel = AuthViewModel(userRepository, dataStore)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given valid credentials when login then update currentUser`() = runTest {
        // Given
        val username = "admin"
        val password = "admin123"
        val user = User(
            id = 1,
            username = username,
            password = password,
            role = UserRole.ADMIN,
            name = "Administrator",
            email = "admin@electric.com",
            phone = "08123456789"
        )

        whenever(userRepository.login(username, password)).thenReturn(user)
        whenever(dataStore.edit(any())).thenReturn(preferences)

        // When
        authViewModel.login(username, password)

        // Then
        assertThat(authViewModel.currentUser.value).isEqualTo(user)
        assertThat(authViewModel.uiState.value.loginSuccess).isTrue()
        assertThat(authViewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `given invalid credentials when login then show error`() = runTest {
        // Given
        val username = "invalid"
        val password = "invalid"

        whenever(userRepository.login(username, password)).thenReturn(null)

        // When
        authViewModel.login(username, password)

        // Then
        assertThat(authViewModel.currentUser.value).isNull()
        assertThat(authViewModel.uiState.value.error).isEqualTo("Username atau password salah")
        assertThat(authViewModel.uiState.value.isLoading).isFalse()
    }
}