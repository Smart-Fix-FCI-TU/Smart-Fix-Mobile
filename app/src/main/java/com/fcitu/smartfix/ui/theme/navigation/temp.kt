package com.fcitu.smartfix.ui.theme.navigation

import androidx.compose.runtime.Composable
import com.fcitu.smartfix.domain.model.UserRole

@Composable
fun LoginScreen(onLoginSuccess: (UserRole) -> Unit) {}
@Composable
fun CustomerHomeScreen(onServiceSelected: () -> Unit) {}
@Composable
fun BookingScreen(onFindTechnician: () -> Unit, onBack: () -> Unit) {}
@Composable
fun TechnicianListScreen(onBack: () -> Unit) {}
@Composable
fun CustomerOrdersScreen(onOrderClick: (String) -> Unit) {}
@Composable
fun CustomerOrderDetailScreen(orderId: String, onBack: () -> Unit) {}
@Composable
fun TrackingScreen(orderId: String, onBack: () -> Unit) {}
@Composable
fun CustomerChatScreen() {}
@Composable
fun CustomerProfileScreen() {}
@Composable
fun TechnicianHomeScreen() {}
@Composable
fun TechnicianOrdersScreen(onOrderClick: (String) -> Unit) {}
@Composable
fun TechnicianOrderDetailScreen(orderId: String, onBack: () -> Unit) {}
@Composable
fun TechnicianChatScreen() {}
@Composable
fun TechnicianProfileScreen() {}