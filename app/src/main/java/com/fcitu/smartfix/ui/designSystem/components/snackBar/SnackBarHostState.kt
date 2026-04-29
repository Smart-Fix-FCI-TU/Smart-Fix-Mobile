package com.fcitu.smartfix.ui.designSystem.components.snackBar

data class SnackBarHostState(
    val isVisible: Boolean = false,
    val snackBarData: SnackBarData = SnackBarData()
)

data class SnackBarData(
    val title: String = "",
    val message: String = "",
    val isError: Boolean = false,
    val duration: Long = 2500
)