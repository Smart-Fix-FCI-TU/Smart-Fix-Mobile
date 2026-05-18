package com.fcitu.smartfix.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
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
import com.fcitu.smartfix.ui.designSystem.components.appBar.AppBar
import com.fcitu.smartfix.ui.designSystem.components.bottomNavigation.BottomNavigationBar
import com.fcitu.smartfix.ui.designSystem.components.scaffold.Scaffold
import com.fcitu.smartfix.ui.designSystem.components.snackBar.AnimatedSnackBarHost
import com.fcitu.smartfix.ui.designSystem.components.snackBar.LocalSnackBarHostController
import com.fcitu.smartfix.ui.designSystem.components.snackBar.SnackBarHostController
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.screen.customer.home.HomeScreen
import com.fcitu.smartfix.ui.screen.customer.myorders.MyOrdersScreen
import com.fcitu.smartfix.ui.screen.customer.profile.CustomerProfileScreen
import com.fcitu.smartfix.ui.screen.customer.profile.CustomerProfileViewModel
import com.fcitu.smartfix.ui.screen.customer.profile.ServiceHistoryScreen
import com.fcitu.smartfix.ui.screen.shared.login.LoginScreen
import com.fcitu.smartfix.ui.screen.shared.orderDetails.OrderDetailsScreen
import com.fcitu.smartfix.ui.screen.shared.splash.SplashScreen
import com.fcitu.smartfix.ui.screen.customer.booking.BookingScreen
import com.fcitu.smartfix.ui.screen.technician.profile.AllReviewsScreen
import com.fcitu.smartfix.ui.screen.technician.profile.TechnicianProfileScreen
import com.fcitu.smartfix.ui.screen.technician.profile.TechnicianProfileViewModel


val LocalNavController = staticCompositionLocalOf<NavController> {
    error("No NavController provided")
}

// TODO: the current graph is just a placeholder to setup navigation structure, remove it later when real screens are implemented
@Composable
fun SmartFixNavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val snackBarHostController = remember { SnackBarHostController() }

    // ── Bottom bar visibility ──────────────────────────
    val bottomBarRoutes = getBottomBarRoutes()

    val showBottomBar = bottomBarRoutes.any { route ->
        currentDestination?.hasRoute(route::class) == true
    }

    // ── Selected tab index ─────────────────────────────
    val isCustomerGraph = isCustomerGraph(currentDestination)

    val selectedIndex = getSelectedIndex(currentDestination)

    // ── Bottom nav tab routes per graph ───────────────
    val customerTabRoutes = getCustomerTabRoutes()

    val technicianTabRoutes = getTechnicianTabRoutes()

    val tabRoutes = if (isCustomerGraph) customerTabRoutes else technicianTabRoutes

    Scaffold(
        bottomBar = {
            BottomBar(
                showBottomBar = showBottomBar,
                selectedIndex = selectedIndex,
                navController = navController,
                tabRoutes = tabRoutes
            )
        },
        snakeBar = {
            AnimatedSnackBarHost(snackBarHostController)
        }
    ) {
        CompositionLocalProvider(
            LocalNavController provides navController,
            LocalSnackBarHostController provides snackBarHostController
        ) {
            NavHost(
                navController = navController,
                startDestination = Route.Splash
            ) {

                // TODO: the current graph is just a placeholder to setup navigation structure, remove it later when real screens are implemented
                // ── Auth ───────────────────────────────────
                composable<Route.Splash> {
                    SplashScreen(
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

                composable<Route.Settings> {
                    // Placeholder Settings Screen to prevent crash
                    Scaffold(
                        topBar = {
                            AppBar(
                                title = "Settings",
                                leadingContent = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_back),
                                        contentDescription = "Back"
                                    )
                                },
                                onLeadingClick = { navController.popBackStack() }
                            )
                        }
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Settings Screen Coming Soon",
                                style = androidx.compose.ui.text.TextStyle(fontFamily = com.fcitu.smartfix.ui.designSystem.theme.Cairo)
                            )
                        }
                    }
                }
                // TODO: the current graph is just a placeholder to setup navigation structure, remove it later when real screens are implemented
                // ── Customer Graph ─────────────────────────
                navigation<Route.CustomerGraph>(
                    startDestination = Route.CustomerHome
                ) {
                    //TODO: Once all the screens that the Home Screen navigates to are built, the code for navigating to these screens will be written.
                    composable<Route.CustomerHome> {
                        HomeScreen(
                            navController = navController
                        )
                    }

                    composable<Route.Booking> { backStackEntry ->
                        val route = backStackEntry.toRoute<Route.Booking>()

                        // استلام البيانات من الخريطة
                        val address = backStackEntry.savedStateHandle.get<String>("selected_location")
                        val lat = backStackEntry.savedStateHandle.get<Double>("selected_lat")
                        val lng = backStackEntry.savedStateHandle.get<Double>("selected_lng")

                        BookingScreen(
                            serviceId = route.serviceId,
                            selectedLocation = address,
                            latitude = lat,
                            longitude = lng,
                            onNavigateBack = { navController.popBackStack() },
                            onNavigateToMap = { navController.navigate(Route.Map) },
                            onProblemSubmitted = {
                                navController.navigate(Route.TechnicianList)
                            }
                        )
                    }

                    composable<Route.Map> {
                        MapScreen(
                            onBack = { navController.popBackStack() },
                            onLocationSelected = { address, lat, lng ->
                                navController.previousBackStackEntry?.savedStateHandle?.set("selected_location", address)
                                navController.previousBackStackEntry?.savedStateHandle?.set("selected_lat", lat)
                                navController.previousBackStackEntry?.savedStateHandle?.set("selected_lng", lng)
                                navController.popBackStack()
                            }
                        )
                    }

                    composable<Route.TechnicianList> {
                        TechnicianListScreen(
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable<Route.CustomerOrders> {
                        MyOrdersScreen(navController = navController)
                    }

                    composable<Route.OrderDetail> {
                        OrderDetailsScreen(
                            userRole = UserRole.CUSTOMER,
                            onBackClicked = { navController.popBackStack() }
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

                    composable<Route.AllServiceHistory> {
                        val viewModel: CustomerProfileViewModel =
                            org.koin.compose.viewmodel.koinViewModel()
                        val state by viewModel.state.collectAsStateWithLifecycle()
                        ServiceHistoryScreen(
                            history = state.serviceHistory,
                            onBack = { navController.popBackStack() }
                        )
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

                    composable<Route.OrderDetail> {
                        OrderDetailsScreen(
                            userRole = UserRole.TECHNICIAN,
                            onBackClicked = { navController.popBackStack() }
                        )
                    }

                    composable<Route.TechnicianChat> {
                        TechnicianChatScreen()
                    }

                    composable<Route.TechnicianProfile> {
                        TechnicianProfileScreen()
                    }

                    composable<Route.AllReviews> {
                        val viewModel: TechnicianProfileViewModel =
                            org.koin.compose.viewmodel.koinViewModel()
                        val state by viewModel.state.collectAsStateWithLifecycle()
                        AllReviewsScreen(
                            reviews = state.reviews,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomBar(
    showBottomBar: Boolean,
    selectedIndex: Int,
    navController: NavHostController,
    tabRoutes: List<Route>
) {
    if (showBottomBar) {
        BottomNavigationBar(
            selectedItemIndex = selectedIndex,
        ) {
            bottomNavigationItem(
                selectedIcon = painterResource(R.drawable.ic_home_selected),
                notSelectedIcon = painterResource(R.drawable.ic_home),
                title = stringResource(R.string.home),
                entry = { navController.navigateToTab(tabRoutes[0]) }
            )
            bottomNavigationItem(
                selectedIcon = painterResource(R.drawable.ic_chat_selected),
                notSelectedIcon = painterResource(R.drawable.ic_chat),
                title = stringResource(R.string.chat),
                entry = { navController.navigateToTab(tabRoutes[1]) }
            )
            bottomNavigationItem(
                selectedIcon = painterResource(R.drawable.ic_orders_selected),
                notSelectedIcon = painterResource(R.drawable.ic_orders),
                title = stringResource(R.string.orders),
                entry = { navController.navigateToTab(tabRoutes[2]) }
            )
            bottomNavigationItem(
                selectedIcon = painterResource(R.drawable.ic_profile_selected),
                notSelectedIcon = painterResource(R.drawable.ic_profile),
                title = stringResource(R.string.profile),
                entry = { navController.navigateToTab(tabRoutes[3]) }
            )
        }
    }
}

@Composable
private fun getTechnicianTabRoutes(): List<Route> = listOf(
    Route.TechnicianHome,
    Route.TechnicianChat,
    Route.TechnicianOrders,
    Route.TechnicianProfile,
)

@Composable
private fun getBottomBarRoutes(): List<Route> = listOf(
    Route.CustomerHome,
    Route.CustomerChat,
    Route.CustomerOrders,
    Route.CustomerProfile,
    Route.TechnicianHome,
    Route.TechnicianChat,
    Route.TechnicianOrders,
    Route.TechnicianProfile,
)

@Composable
private fun getSelectedIndex(currentDestination: NavDestination?): Int = when {
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

@Composable
private fun isCustomerGraph(currentDestination: NavDestination?): Boolean =
    getCustomerTabRoutes().any { currentDestination?.hasRoute(it::class) == true }

@Composable
private fun getCustomerTabRoutes(): List<Route> = listOf(
    Route.CustomerHome,
    Route.CustomerChat,
    Route.CustomerOrders,
    Route.CustomerProfile,
)

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