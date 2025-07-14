package com.fathi.electricitybillapp.compose.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.fathi.electricitybillapp.data.usage.ElectricUsage
import com.fathi.electricitybillapp.util.getMonthName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsageDialog(
    usage: ElectricUsage?,
    onDismiss: () -> Unit,
    onSave: (ElectricUsage) -> Unit
) {
    var customerId by remember { mutableStateOf(usage?.customerId ?: "") }
    var month by remember { mutableStateOf(usage?.month ?: 1) }
    var year by remember { mutableStateOf(usage?.year ?: 2024) }
    var previousReading by remember { mutableStateOf(usage?.previousReading?.toString() ?: "") }
    var currentReading by remember { mutableStateOf(usage?.currentReading?.toString() ?: "") }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (usage == null) "Tambah Penggunaan" else "Edit Penggunaan") },
        text = {
            Column {
                OutlinedTextField(
                    value = customerId,
                    onValueChange = { customerId = it },
                    label = { Text("ID Pelanggan") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Month Dropdown
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = getMonthName(month),
                        onValueChange = {},
                        label = { Text("Bulan") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        (1..12).forEach { monthValue ->
                            DropdownMenuItem(
                                text = { Text(getMonthName(monthValue)) },
                                onClick = {
                                    month = monthValue
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = year.toString(),
                    onValueChange = { year = it.toIntOrNull() ?: year },
                    label = { Text("Tahun") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = previousReading,
                    onValueChange = { previousReading = it },
                    label = { Text("Meteran Awal (kWh)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = currentReading,
                    onValueChange = { currentReading = it },
                    label = { Text("Meteran Akhir (kWh)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val previousReadingDouble = previousReading.toDoubleOrNull() ?: 0.0
                    val currentReadingDouble = currentReading.toDoubleOrNull() ?: 0.0

                    if (customerId.isNotBlank() &&
                        currentReadingDouble > previousReadingDouble) {

                        val newUsage = ElectricUsage(
                            id = usage?.id ?: 0,
                            customerId = customerId,
                            month = month,
                            year = year,
                            previousReading = previousReadingDouble,
                            currentReading = currentReadingDouble,
                            usageKwh = currentReadingDouble - previousReadingDouble,
                            totalAmount = (currentReadingDouble - previousReadingDouble) * 1467.28,
                            dueDate = System.currentTimeMillis() + (30 * 24 * 60 * 60 * 1000L) // 30 days
                        )
                        onSave(newUsage)
                    }
                }
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}