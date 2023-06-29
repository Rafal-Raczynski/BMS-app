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
    var unit: String,
    var icon: ImageVector
)
{
    fun updateValue( value: String) {
        this.value = value
    }
}

class Master(){
    val status=Data("Status", "ON"," ", Icons.Outlined.Power)
    val voltage=Data("Voltage", "a", " V",Icons.Outlined.BatteryChargingFull)
    val current=Data("Current", "1"," A",Icons.Outlined.Bolt)
    val energy=Data("Energy", "0.5", " kWh",Icons.Outlined.Memory)
    val temperature=Data("Temperature", "24"," °C", Icons.Outlined.Thermostat)
    val sof=Data("State of Health", "94"," %", Icons.Outlined.HealthAndSafety)
    val soc=Data("State of Charge", "10"," ", Icons.Outlined.HealthAndSafety)
}


class Slave() {
    val cells = Data("No. of cells", "5", "", Icons.Outlined.Power)
    val minimumvoltage = Data("Minimum Voltage", "3.20 V", "", Icons.Outlined.Battery1Bar)
    val maximumvoltage = Data("Maximum Voltage", "3.30 V", "", Icons.Outlined.BatteryFull)
    val energy = Data("Energy", "0.5 kWh", "", Icons.Outlined.Memory)
    val voltage = Data("Voltage", "3.27 V", "", Icons.Outlined.Bolt)
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