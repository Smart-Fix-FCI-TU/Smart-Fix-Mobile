package com.fcitu.smartfix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.fcitu.smartfix.ui.designSystem.components.snackBar.LocalSnackBarHostController
import com.fcitu.smartfix.ui.designSystem.components.snackBar.SnackBarHostController
import com.fcitu.smartfix.ui.designSystem.theme.SmartFixTheme
import com.fcitu.smartfix.ui.navigation.SmartFixNavGraph
import com.fcitu.smartfix.ui.screen.customer.home.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val snackBarHostController = remember { SnackBarHostController() }

            SmartFixTheme {
                // 2. بنزود الـ Controller لكل الـ Children اللي جوه التطبيق
                CompositionLocalProvider(
                    LocalSnackBarHostController provides snackBarHostController
                ) {
                    SmartFixNavGraph()
                }
        }
    }
}}