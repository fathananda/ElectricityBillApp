package com.fathi.electricitybillapp.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fathi.electricitybillapp.data.usage.ElectricUsage
import com.fathi.electricitybillapp.util.getMonthName

@Composable
fun ElectricUsageCard(
    usage: ElectricUsage,
    onEdit: ((ElectricUsage) -> Unit)?,
    onDelete: ((ElectricUsage) -> Unit)?,
    isAdmin: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "ID: ${usage.customerId}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${getMonthName(usage.month)} ${usage.year}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Meteran Awal:")
                Text("${usage.previousReading.toInt()} kWh")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Meteran Akhir:")
                Text("${usage.currentReading.toInt()} kWh")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Penggunaan:")
                Text("${usage.usageKwh.toInt()} kWh")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Tarif per kWh:")
                Text("Rp ${String.format("%,.0f", usage.ratePerKwh)}")
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total:",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Rp ${String.format("%,.0f", usage.totalAmount)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (isAdmin && (onEdit != null || onDelete != null)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (onEdit != null) {
                        TextButton(onClick = { onEdit(usage) }) {
                            Text("Edit")
                        }
                    }
                    if (onDelete != null) {
                        TextButton(onClick = { onDelete(usage) }) {
                            Text("Hapus")
                        }
                    }
                }
            }
        }
    }
}