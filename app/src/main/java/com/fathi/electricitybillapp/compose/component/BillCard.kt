package com.fathi.electricitybillapp.compose.component


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fathi.electricitybillapp.data.bill.Bill
import com.fathi.electricitybillapp.util.formatDate
import com.fathi.electricitybillapp.util.getMonthName

@Composable
fun BillCard(
    bill: Bill,
    onPay: ((Bill) -> Unit)?,
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
                    text = "ID: ${bill.customerId}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${getMonthName(bill.month)} ${bill.year}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Penggunaan:")
                Text("${bill.usageKwh.toInt()} kWh")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Biaya Listrik:")
                Text("Rp ${String.format("%,.0f", bill.amount)}")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Biaya Admin:")
                Text("Rp ${String.format("%,.0f", bill.adminFee)}")
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
                    text = "Total Tagihan:",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Rp ${String.format("%,.0f", bill.totalAmount)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Status:")
                Text(
                    text = if (bill.isPaid) "LUNAS" else "BELUM LUNAS",
                    color = if (bill.isPaid) Color.Green else Color.Red
                )
            }

            if (!bill.isPaid) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Jatuh Tempo:")
                    Text(formatDate(bill.dueDate))
                }
            }

            if (!isAdmin && !bill.isPaid && onPay != null) {
                Button(
                    onClick = { onPay(bill) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Bayar Sekarang")
                }
            }
        }
    }
}