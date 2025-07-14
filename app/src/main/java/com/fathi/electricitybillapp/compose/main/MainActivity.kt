package com.fathi.electricitybillapp.compose.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.fathi.electricitybillapp.app.ElectricBillApp
import com.fathi.electricitybillapp.ui.theme.ElectricityBillAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ElectricityBillAppTheme {
                ElectricBillApp()
            }
        }
    }
}