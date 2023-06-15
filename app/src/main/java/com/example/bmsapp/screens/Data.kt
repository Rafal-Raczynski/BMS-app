package com.example.bmsapp.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.delay
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

data class Data(
    var label: String,
    var value: String,
    var icon: ImageVector
)
{
    fun updateValue( value: String) {
        this.value = value
    }
}

class Master(){
    val status=Data("Status", "ON", Icons.Outlined.Power)
    val voltage=Data("Voltage", "a", Icons.Outlined.BatteryChargingFull)
    val current=Data("Current", "1 A", Icons.Outlined.Bolt)
    val energy=Data("Energy", "0.5kWh", Icons.Outlined.Memory)
    val temperature=Data("Temperature", "24°C", Icons.Outlined.Thermostat)
    val sof=Data("State of Health", "94 %", Icons.Outlined.HealthAndSafety)
    val soc=Data("State of Charge", "10", Icons.Outlined.HealthAndSafety)
}




@Composable
fun randomFloat(): String {
    var randomFloat by remember { mutableStateOf(30.00f) }
    LaunchedEffect(true) {
        while (true) {
            randomFloat = (Random().nextFloat() * 20.10f + 30.00f).round(2)
           delay(5000)
        }
    }

    return String.format("%.2f", randomFloat)
}



fun Float.round(decimalPlaces: Int): Float {
    val decimalFormat = DecimalFormat("#.${"0".repeat(decimalPlaces)}")
    decimalFormat.roundingMode = RoundingMode.HALF_UP
    return decimalFormat.format(this).toFloat()
}