package com.fcitu.smartfix.ui.navigation

import androidx.compose.runtime.Composable


// TODO: Don't use this screens that are used to setup navigation graph, remove them later when real screens are implemented
@Composable
fun CustomerHomeScreen(onServiceSelected: (String) -> Unit) {}

@Composable
fun MapScreen(onBack: () -> Unit, onLocationSelected: (String, Double, Double) -> Unit) {}

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

// تم تعطيل هذه الشاشات مؤقتاً لأنها موجودة فعلياً في ملفات أخرى وتسبب تعارض في الـ Build
// @Composable
// fun CustomerProfileScreen() {}

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

// تم تعطيل هذه الشاشات مؤقتاً لأنها موجودة فعلياً في ملفات أخرى وتسبب تعارض في الـ Build
// @Composable
// fun TechnicianProfileScreen() {}
