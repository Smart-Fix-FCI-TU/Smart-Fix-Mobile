package com.fcitu.smartfix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fcitu.smartfix.ui.designSystem.theme.SmartFixTheme
import com.fcitu.smartfix.ui.navigation.SmartFixNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartFixTheme {
                SmartFixNavGraph()
            }
        }
    }
}