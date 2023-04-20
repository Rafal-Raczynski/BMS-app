package com.example.bmsapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BluetoothSearching
import androidx.compose.material.icons.filled.BatteryUnknown
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ViewHeadline
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Bluetooth : BottomBarScreen(
        route = "bluetooth",
        title = "Bluetooth",
        icon = Icons.Default.BluetoothSearching
    )

    object Status : BottomBarScreen(
        route = "status",
        title = "Status",
        icon = Icons.Default.BatteryUnknown
    )

    object Details : BottomBarScreen(
        route = "details",
        title = "Details",
        icon = Icons.Default.ViewHeadline
    )

    object History : BottomBarScreen(
        route = "history",
        title = "History",
        icon = Icons.Default.History
    )
}
