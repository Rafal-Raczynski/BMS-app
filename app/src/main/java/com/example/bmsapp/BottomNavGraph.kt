package com.example.bmsapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bmsapp.screens.BluetoothScreen
import com.example.bmsapp.screens.StatusScreen
import com.example.bmsapp.screens.HistoryScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Bluetooth.route
    ) {
        composable(route = BottomBarScreen.Bluetooth.route) {
            BluetoothScreen()
        }
        composable(route = BottomBarScreen.Status.route) {
            StatusScreen()
        }
        composable(route = BottomBarScreen.History.route) {
            HistoryScreen()
        }
    }
}