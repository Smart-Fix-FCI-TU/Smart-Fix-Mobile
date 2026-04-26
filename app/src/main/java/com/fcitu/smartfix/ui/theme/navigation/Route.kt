package com.fcitu.smartfix.ui.theme.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    // ── Auth ──────────────────────────────────────────
    @Serializable data object Splash : Route
    @Serializable data object Login : Route

    // ── Customer Graph ────────────────────────────────
    @Serializable data object CustomerGraph : Route
    @Serializable data object CustomerHome : Route
    @Serializable data object CustomerChat : Route
    @Serializable data object CustomerOrders : Route
    @Serializable data object CustomerProfile : Route
    @Serializable data object Booking : Route
    @Serializable data object TechnicianList : Route
    @Serializable data class OrderDetail(val orderId: String) : Route
    @Serializable data class Tracking(val orderId: String) : Route

    // ── Technician Graph ──────────────────────────────
    @Serializable data object TechnicianGraph : Route
    @Serializable data object TechnicianHome : Route
    @Serializable data object TechnicianChat : Route
    @Serializable data object TechnicianOrders : Route
    @Serializable data object TechnicianProfile : Route
    @Serializable data class TechnicianOrderDetail(val orderId: String) : Route
}