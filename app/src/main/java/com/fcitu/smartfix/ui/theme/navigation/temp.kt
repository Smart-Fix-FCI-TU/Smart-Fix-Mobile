package com.fcitu.smartfix.ui.theme.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


// TODO: Don't use this screens that are used to setup navigation graph, remove them later when real screens are implemented
@Composable
fun CustomerHomeScreen(onServiceSelected: (String) -> Unit) {}
@Composable
fun BookingScreen(serviceId: String, onNavigateBack: () -> Unit, onNavigateToMap: () -> Unit, onProblemSubmitted: () -> Unit) {}

@Composable
fun MapScreen(onBack: () -> Unit, onLocationSelected: (String, Double, Double) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Select Location (Simulator)")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { 
                // إحداثيات تجريبية (المعادي، القاهرة)
                onLocationSelected("Tahrir St, Maadi, Cairo", 29.9602, 31.2569) 
            }) {
                Text("Pick Current Location")
            }
            TextButton(onClick = onBack) {
                Text("Go Back")
            }
        }
    }
}

// TODO: Don't use this screens that are used to setup navigation graph, remove them later when real screens are implemented
@Composable
fun TechnicianListScreen(onBack: () -> Unit) {}
// TODO: Don't use this screens that are used to setup navigation graph, remove them later when real screens are implemented
@Composable
fun CustomerOrdersScreen(onOrderClick: (String) -> Unit) {}
// TODO: Don't use this screens that are used to setup navigation graph, remove them later when real screens are implemented
@Composable
fun CustomerOrderDetailScreen(orderId: String, onBack: () -> Unit) {}
@Composable
fun TrackingScreen(orderId: String, onBack: () -> Unit) {}
// TODO: Don't use this screens that are used to setup navigation graph, remove them later when real screens are implemented
@Composable
fun CustomerChatScreen() {}
@Composable
fun CustomerProfileScreen() {}
// TODO: Don't use this screens that are used to setup navigation graph, remove them later when real screens are implemented
@Composable
fun TechnicianHomeScreen() {}
@Composable
fun TechnicianOrdersScreen(onOrderClick: (String) -> Unit) {}
// TODO: Don't use this screens that are used to setup navigation graph, remove them later when real screens are implemented
@Composable
fun TechnicianOrderDetailScreen(orderId: String, onBack: () -> Unit) {}
@Composable
fun TechnicianChatScreen() {}
// TODO: Don't use this screens that are used to setup navigation graph, remove them later when real screens are implemented
@Composable
fun TechnicianProfileScreen() {}