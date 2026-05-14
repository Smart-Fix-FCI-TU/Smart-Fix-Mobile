package com.fcitu.smartfix.ui.navigation

import androidx.compose.runtime.Composable


@Composable
fun BookingScreen(onFindTechnician: () -> Unit, onBack: () -> Unit) {}
// TODO: Don't use this screens that are used to setup navigation graph, remove them later when real screens are implemented

@Composable
fun CustomerOrdersScreen(onOrderClick: (String) -> Unit) {}
// TODO: Don't use this screens that are used to setup navigation graph, remove them later when real screens are implemented
@Composable
fun TrackingScreen(orderId: String, onBack: () -> Unit) {}
// TODO: Don't use this screens that are used to setup navigation graph, remove them later when real screens are implemented
@Composable
fun CustomerChatScreen() {}
// TODO: Don't use this screens that are used to setup navigation graph, remove them later when real screens are implemented
@Composable
fun TechnicianHomeScreen() {}
@Composable
fun TechnicianOrdersScreen(onOrderClick: (String) -> Unit) {}
// TODO: Don't use this screens that are used to setup navigation graph, remove them later when real screens are implemented
@Composable
fun TechnicianChatScreen() {}
// TODO: Don't use this screens that are used to setup navigation graph, remove them later when real screens are implemented