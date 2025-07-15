package com.fathi.electricitybillapp.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fathi.electricitybillapp.compose.admin.AdminBillScreen
import com.fathi.electricitybillapp.compose.admin.AdminUsageScreen
import com.fathi.electricitybillapp.compose.customer.CustomerBillScreen
import com.fathi.electricitybillapp.compose.customer.CustomerUsageScreen
import com.fathi.electricitybillapp.data.user.User
import com.fathi.electricitybillapp.data.user.UserRole
import com.fathi.electricitybillapp.viewmodel.auth.AuthViewModel

/**
 * Modul utama aplikasi pembayaran listrik pascabayar
 *
 * Kegunaan:
 * - Mengelola navigasi utama aplikasi
 * - Menentukan tampilan berdasarkan status autentikasi
 * - Menyediakan antarmuka login dan navigasi utama
 *
 * @author Fathi
 * @since 2025
 */

/**
 * Composable utama aplikasi
 *
 * Fungsi:
 * - Menentukan tampilan berdasarkan status login
 * - Mengelola navigasi antara LoginScreen dan MainNavigationScreen
 *
 * @return Unit
 *
 * Exceptions:
 * - Tidak ada exception khusus
 *
 * Dependencies:
 * - AuthViewModel untuk status autentikasi
 * - Compose UI untuk rendering
 */

@Composable
fun ElectricBillApp() {
    val authViewModel: AuthViewModel = hiltViewModel()
    val currentUser by authViewModel.currentUser.collectAsState()

    if (currentUser == null) {
        LoginScreen(authViewModel = authViewModel)
    } else {
        MainNavigationScreen(
            user = currentUser!!,
            onLogout = { authViewModel.logout() }
        )
    }
}


/**
 * Tampilan login aplikasi
 *
 * @param authViewModel ViewModel untuk autentikasi
 *
 * Fungsi:
 * - Menampilkan form login
 * - Validasi input username dan password
 * - Menampilkan pesan error jika login gagal
 * - Menyediakan informasi akun demo
 *
 * @return Unit
 *
 * Exceptions:
 * - LoginException: Jika kredensial tidak valid
 * - NetworkException: Jika terjadi masalah koneksi
 */

@Composable
fun LoginScreen(authViewModel: AuthViewModel) {
    val uiState by authViewModel.uiState.collectAsState()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            // Show error message
            authViewModel.clearError()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Aplikasi Pembayaran Listrik",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { authViewModel.login(username, password) },
            enabled = !uiState.isLoading && username.isNotBlank() && password.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp))
            } else {
                Text("Login")
            }
        }

        uiState.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Demo Accounts:\nAdmin: admin/admin123\nCustomer: customer/customer123",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


/**
 * Tampilan navigasi utama setelah login
 *
 * @param user Pengguna yang sedang login
 * @param onLogout Callback untuk logout
 *
 * Fungsi:
 * - Menampilkan tab navigation berdasarkan role
 * - Mengelola perpindahan antar screen
 * - Menyediakan fungsi logout
 *
 * @return Unit
 *
 * Exceptions:
 * - Tidak ada exception khusus
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigationScreen(user: User, onLogout: () -> Unit) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top App Bar
        TopAppBar(
            title = { Text("Electric Bill App") },
            actions = {
                TextButton(onClick = onLogout) {
                    Text("Logout")
                }
            }
        )

        // Navigation Tabs
        TabRow(selectedTabIndex = selectedTab) {
            if (user.role == UserRole.ADMIN) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                ) {
                    Text("Kelola Penggunaan", modifier = Modifier.padding(16.dp))
                }
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                ) {
                    Text("Semua Tagihan", modifier = Modifier.padding(16.dp))
                }
            } else {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                ) {
                    Text("Penggunaan Saya", modifier = Modifier.padding(16.dp))
                }
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                ) {
                    Text("Tagihan Saya", modifier = Modifier.padding(16.dp))
                }
            }
        }

        // Content based on selected tab
        when (selectedTab) {
            0 -> {
                if (user.role == UserRole.ADMIN) {
                    AdminUsageScreen()
                } else {
                    CustomerUsageScreen(customerId = user.customerId!!)
                }
            }
            1 -> {
                if (user.role == UserRole.ADMIN) {
                    AdminBillScreen()
                } else {
                    CustomerBillScreen(customerId = user.customerId!!)
                }
            }
        }
    }
}