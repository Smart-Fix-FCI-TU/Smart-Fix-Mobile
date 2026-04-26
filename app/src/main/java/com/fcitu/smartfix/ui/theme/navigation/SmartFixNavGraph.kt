package com.fcitu.smartfix.ui.theme.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.fcitu.smartfix.R
import com.fcitu.smartfix.domain.model.UserRole
import com.fcitu.smartfix.ui.theme.designSystem.components.bottomNavigation.BottomNavigationBar
import com.fcitu.smartfix.ui.theme.designSystem.components.scaffold.Scaffold

@Composable
fun SmartFixNavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // ── Bottom bar visibility ──────────────────────────
    val bottomBarRoutes = listOf(
        Route.CustomerHome,
        Route.CustomerChat,
        Route.CustomerOrders,
        Route.CustomerProfile,
        Route.TechnicianHome,
        Route.TechnicianChat,
        Route.TechnicianOrders,
        Route.TechnicianProfile,
    )

    val showBottomBar = bottomBarRoutes.any { route ->
        currentDestination?.hasRoute(route::class) == true
    }

    // ── Selected tab index ─────────────────────────────
    val isCustomerGraph = listOf(
        Route.CustomerHome,
        Route.CustomerChat,
        Route.CustomerOrders,
        Route.CustomerProfile,
    ).any { currentDestination?.hasRoute(it::class) == true }

    val selectedIndex = when {
        currentDestination?.hasRoute(Route.CustomerHome::class) == true -> 0
        currentDestination?.hasRoute(Route.CustomerChat::class) == true -> 1
        currentDestination?.hasRoute(Route.CustomerOrders::class) == true -> 2
        currentDestination?.hasRoute(Route.CustomerProfile::class) == true -> 3
        currentDestination?.hasRoute(Route.TechnicianHome::class) == true -> 0
        currentDestination?.hasRoute(Route.TechnicianChat::class) == true -> 1
        currentDestination?.hasRoute(Route.TechnicianOrders::class) == true -> 2
        currentDestination?.hasRoute(Route.TechnicianProfile::class) == true -> 3
        else -> 0
    }

    // ── Bottom nav tab routes per graph ───────────────
    val customerTabRoutes = listOf(
        Route.CustomerHome,
        Route.CustomerChat,
        Route.CustomerOrders,
        Route.CustomerProfile,
    )

    val technicianTabRoutes = listOf(
        Route.TechnicianHome,
        Route.TechnicianChat,
        Route.TechnicianOrders,
        Route.TechnicianProfile,
    )

    val tabRoutes = if (isCustomerGraph) customerTabRoutes else technicianTabRoutes

    // ── Scaffold ───────────────────────────────────────
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    selectedItemIndex = selectedIndex,
                ) {
                    bottomNavigationItem(
                        selectedIcon = painterResource(R.drawable.ic_home_selected),
                        notSelectedIcon = painterResource(R.drawable.ic_home),
                        title = "Home",
                        entry = { navController.navigateToTab(tabRoutes[0]) }
                    )
                    bottomNavigationItem(
                        selectedIcon = painterResource(R.drawable.ic_chat_selected),
                        notSelectedIcon = painterResource(R.drawable.ic_chat),
                        title = "Chat",
                        entry = { navController.navigateToTab(tabRoutes[1]) }
                    )
                    bottomNavigationItem(
                        selectedIcon = painterResource(R.drawable.ic_orders_selected),
                        notSelectedIcon = painterResource(R.drawable.ic_orders),
                        title = "Orders",
                        entry = { navController.navigateToTab(tabRoutes[2]) }
                    )
                    bottomNavigationItem(
                        selectedIcon = painterResource(R.drawable.ic_profile_selected),
                        notSelectedIcon = painterResource(R.drawable.ic_profile),
                        title = "Profile",
                        entry = { navController.navigateToTab(tabRoutes[3]) }
                    )
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Route.Splash,
        ) {

            // ── Auth ───────────────────────────────────
            composable<Route.Splash> {
                android.window.SplashScreen(
                    onNavigateToLogin = {
                        navController.navigate(Route.Login) {
                            popUpTo(Route.Splash) { inclusive = true }
                        }
                    },
                    onNavigateToCustomerHome = {
                        navController.navigate(Route.CustomerGraph) {
                            popUpTo(Route.Splash) { inclusive = true }
                        }
                    },
                    onNavigateToTechnicianHome = {
                        navController.navigate(Route.TechnicianGraph) {
                            popUpTo(Route.Splash) { inclusive = true }
                        }
                    }
                )
            }

            composable<Route.Login> {
                LoginScreen(
                    onLoginSuccess = { role ->
                        val destination = if (role == UserRole.CUSTOMER) Route.CustomerGraph
                        else Route.TechnicianGraph
                        navController.navigate(destination) {
                            popUpTo(Route.Login) { inclusive = true }
                        }
                    }
                )
            }

            // ── Customer Graph ─────────────────────────
            navigation<Route.CustomerGraph>(
                startDestination = Route.CustomerHome
            ) {
                composable<Route.CustomerHome> {
                    CustomerHomeScreen(
                        onServiceSelected = {
                            navController.navigate(Route.Booking)
                        }
                    )
                }

                composable<Route.Booking> {
                    BookingScreen(
                        onFindTechnician = {
                            navController.navigate(Route.TechnicianList)
                        },
                        onBack = { navController.popBackStack() }
                    )
                }

                composable<Route.TechnicianList> {
                    TechnicianListScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                composable<Route.CustomerOrders> {
                    CustomerOrdersScreen(
                        onOrderClick = { orderId ->
                            navController.navigate(Route.OrderDetail(orderId))
                        }
                    )
                }

                composable<Route.OrderDetail> { backStackEntry ->
                    val route = backStackEntry.toRoute<Route.OrderDetail>()
                    CustomerOrderDetailScreen(
                        orderId = route.orderId,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable<Route.Tracking> { backStackEntry ->
                    val route = backStackEntry.toRoute<Route.Tracking>()
                    TrackingScreen(
                        orderId = route.orderId,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable<Route.CustomerChat> {
                    CustomerChatScreen()
                }

                composable<Route.CustomerProfile> {
                    CustomerProfileScreen()
                }
            }

            // ── Technician Graph ───────────────────────
            navigation<Route.TechnicianGraph>(
                startDestination = Route.TechnicianHome
            ) {
                composable<Route.TechnicianHome> {
                    TechnicianHomeScreen()
                }

                composable<Route.TechnicianOrders> {
                    TechnicianOrdersScreen(
                        onOrderClick = { orderId ->
                            navController.navigate(Route.TechnicianOrderDetail(orderId))
                        }
                    )
                }

                composable<Route.TechnicianOrderDetail> { backStackEntry ->
                    val route = backStackEntry.toRoute<Route.TechnicianOrderDetail>()
                    TechnicianOrderDetailScreen(
                        orderId = route.orderId,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable<Route.TechnicianChat> {
                    TechnicianChatScreen()
                }

                composable<Route.TechnicianProfile> {
                    TechnicianProfileScreen()
                }
            }
        }
    }
}

// ── Extension: tab navigation ──────────────────────────
private fun NavHostController.navigateToTab(route: Route) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}