package com.fathi.electricitybillapp.compose.admin

import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fathi.electricitybillapp.data.usage.ElectricUsage
import com.fathi.electricitybillapp.compose.component.ElectricUsageCard
import com.fathi.electricitybillapp.viewmodel.usage.ElectricUsageViewModel
import com.fathi.electricitybillapp.compose.component.UsageDialog

@Composable
fun AdminUsageScreen() {
    val viewModel: ElectricUsageViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingUsage by remember { mutableStateOf<ElectricUsage?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadUsages()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header with Add Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Kelola Penggunaan Listrik",
                style = MaterialTheme.typography.headlineSmall
            )
            FloatingActionButton(
                onClick = { showAddDialog = true },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah")
            }
        }

        // Content
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.usages) { usage ->
                    ElectricUsageCard(
                        usage = usage,
                        onEdit = { editingUsage = it },
                        onDelete = { viewModel.deleteUsage(it) },
                        isAdmin = true
                    )
                }
            }
        }
    }

    // Add/Edit Dialog
    if (showAddDialog || editingUsage != null) {
        UsageDialog(
            usage = editingUsage,
            onDismiss = {
                showAddDialog = false
                editingUsage = null
            },
            onSave = { usage ->
                viewModel.saveUsage(usage)
                showAddDialog = false
                editingUsage = null
            }
        )
    }

    // Handle messages
    LaunchedEffect(uiState.saveSuccess, uiState.deleteSuccess, uiState.error) {
        if (uiState.saveSuccess || uiState.deleteSuccess || uiState.error != null) {
            viewModel.clearMessages()
        }
    }
}
