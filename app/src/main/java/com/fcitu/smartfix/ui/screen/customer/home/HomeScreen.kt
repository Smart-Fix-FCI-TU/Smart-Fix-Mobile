package com.fcitu.smartfix.ui.screen.customer.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.domain.entity.Address
import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.entity.ServiceItem
import com.fcitu.smartfix.domain.entity.User
import com.fcitu.smartfix.domain.model.OrderStatus
import com.fcitu.smartfix.domain.model.ServiceCategory
import com.fcitu.smartfix.domain.model.UserRole
import com.fcitu.smartfix.ui.designSystem.components.scaffold.Scaffold
import com.fcitu.smartfix.ui.designSystem.components.snackBar.AnimatedSnackBarHost
import com.fcitu.smartfix.ui.designSystem.components.snackBar.LocalSnackBarHostController
import com.fcitu.smartfix.ui.designSystem.components.snackBar.SnackBarData
import com.fcitu.smartfix.ui.screen.customer.home.component.HomeAppBar
import com.fcitu.smartfix.ui.screen.customer.home.component.NetworkOutageScreen
import com.fcitu.smartfix.ui.screen.customer.home.component.OrderSection
import com.fcitu.smartfix.ui.screen.customer.home.component.PendingOrderSection
import com.fcitu.smartfix.ui.screen.customer.home.component.ServicesSection
import com.fcitu.smartfix.ui.screen.customer.home.component.UserProfileHeader
import kotlinx.datetime.LocalDateTime
import org.koin.androidx.compose.koinViewModel
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun HomeScreen(
    onNavigateToBooking: (String) -> Unit = {},
    onNavigateToOrderDetails: (String) -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToAllOrders: () -> Unit = {},
    onNavigateToTechniciansList: (String) -> Unit = {},
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeUiEffect.NavigateToBooking ->
                    onNavigateToBooking(effect.selectedCategory)

                is HomeUiEffect.NavigateToOrderDetails ->
                    onNavigateToOrderDetails(effect.orderId)

                is HomeUiEffect.NavigateToNotifications ->
                    onNavigateToNotifications()

                is HomeUiEffect.NavigateToAllActiveOrders ->
                    onNavigateToAllOrders()

                is HomeUiEffect.NavigateToAvailableTechnicianList -> {
                    onNavigateToTechniciansList(effect.orderId)
                }

                is HomeUiEffect.ShowError -> {

                }

            }
        }
    }
    HomeContent(
        state = state,
        listener = viewModel
    )
}

@Composable
private fun HomeContent(
    state: HomeUiState,
    listener: HomeInteractionListener,
) {
    val snackBarHostController = LocalSnackBarHostController.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = Color(0xFFF2F4F7),
        statusBarColor = Color.Transparent,
        topBar = {
            HomeAppBar(title = "Smart Fix", leadingContent = {
                Image(
                    painter = painterResource(R.drawable.notification_icon),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp)
                )
            }, onLeadingClick = { listener.onNotificationClicked() })
        },
        snakeBar = {
            AnimatedSnackBarHost(
                snackBarHostController = snackBarHostController,
                modifier = Modifier.padding(top = 60.dp)
            )
        }
    ) {
        if (state.hasNetworkConnection) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = scrollState),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                UserProfileHeader(state.userState, Modifier.padding(horizontal = 16.dp))

                if (state.hasPendingOrder) {
                    PendingOrderSection(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        orderId = state.pendingOrderId!! // هنا الـ !! آمنة عشان الشرط اللي فوق بيمنع إنها تكون null
                    ) {
                        listener.onNavigateToAvailableTechnicianList(state.pendingOrderId)
                    }
                }

                OrderSection(state, listener, Modifier.padding(horizontal = 16.dp))
                ServicesSection(state, listener, Modifier.padding(horizontal = 16.dp))
            }
        } else {
            NetworkOutageScreen()
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Preview(showSystemUi = true)
@Composable
private fun test() {
HomeContent(state = HomeUiState(
    userState =UserProfileState.Success(user = User(
        id = "5425425",
        phoneNumber = "563767567262",
        firstName = "Fouad",
        lastName = "Elmeligy",
        username = "Fouad Elmeligy",
        birthOfDate = "2/2/2002",
        nationalId = "25362627246",
        email = "fouad@gmail.com",
        role = UserRole.CUSTOMER,
        profilePhotoUrl = "",
        address = Address(
            id = "523455",
            fullAddress = "Tanta",
            location = Address.Location(30.0, 31.0),
            floor = "1",
            apartmentNo = "2"
        )
    )),
    activeOrders = List(4) {
        Order(
            id = Uuid.random().toString(),
            customer = Order.UserInfo(id = Uuid.random().toString(), name = "Fouad"),
            technician = Order.UserInfo(id = Uuid.random().toString(), name = "Ahmed Mohamed"),
            details = Order.OrderDetails(
                serviceCategory = ServiceCategory.ELECTRICITY,
                title = "change lamb",
                "the lamb is broken", problemPhotoUrls = emptyList(),
                address = Address(
                    id = Uuid.random().toString(),
                    fullAddress = "Tanta",
                    location = Address.Location(40.0, 41.0),
                    floor = "2",
                    apartmentNo = "3"
                ), additionalNotes = ""
            ),
            repairPhotos = Order.RepairPhotos(
                beforeRepairUrls = emptyList(),
                afterRepairUrls = emptyList()
            ),
            status = OrderStatus.ON_WAY, timeline = Order.OrderTimeline(
                createdAt   = LocalDateTime(2024, 1, 15, 10, 30),
                acceptedAt  = LocalDateTime(2024, 1, 15, 11, 0),
                arrivedAt   = LocalDateTime(2024, 1, 15, 12, 0),
                startedAt   = LocalDateTime(2024, 1, 15, 12, 30),
                completedAt = LocalDateTime(2024, 1, 15, 12, 30),
            )
        )
    },
    pendingOrderId = "",
    servicesList =  listOf(
        ServiceItem(
            serviceCategory = ServiceCategory.ELECTRICITY,
            serviceName = "Electricity",
            R.drawable.electricity_icon_active,
            R.drawable.electricity_icon
        ),
        ServiceItem(
            serviceCategory = ServiceCategory.PLUMBING,
            "Plumbing",
            R.drawable.plumbing_icon_active,
            R.drawable.plumbing_icon
        ),
        ServiceItem(
            serviceCategory = ServiceCategory.CONDITIONING,
            "Conditioning",
            R.drawable.conditioning_icon_active,
            R.drawable.conditioning_icon
        ),
        ServiceItem(
            serviceCategory = ServiceCategory.PAINTING,
            "Paints",
            R.drawable.painting_icon_active,
            R.drawable.painting_icon
        ),
        ServiceItem(
            serviceCategory = ServiceCategory.CARPENTRY,
            "Carpentry",
            R.drawable.carpentry_icon_active,
            R.drawable.carpentry_icon
        ),

        )
),
    listener = object : HomeInteractionListener {
        override fun onCategorySelected(category: String) {
            TODO("Not yet implemented")
        }

        override fun onChooseServiceClicked() {
            TODO("Not yet implemented")
        }

        override fun onOrderClicked(orderId: String) {
            TODO("Not yet implemented")
        }

        override fun onNotificationClicked() {
            TODO("Not yet implemented")
        }

        override fun onViewAllOrdersClicked(orders: List<Order>) {
            TODO("Not yet implemented")
        }

        override fun onNavigateToAvailableTechnicianList(orderId: String) {
            TODO("Not yet implemented")
        }
    })
}