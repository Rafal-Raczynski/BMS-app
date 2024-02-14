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
    val power=Data("Power", "0.5", " W",Icons.Outlined.Memory)
    val temperature=Data("Temperature", "24"," °C", Icons.Outlined.Thermostat)
    val remainingenergy=Data("Remaining Energy", "94"," kWh", Icons.Outlined.HealthAndSafety)
    val soc=Data("State of Charge", "10"," ", Icons.Outlined.HealthAndSafety)
    val cellvolt=Data("Cell Voltage", "a", " V",Icons.Outlined.BatteryChargingFull)
    val celltemp=Data("Cell Temp", "1"," °C",Icons.Outlined.Thermostat)
}


class Slave() {
    val cells = Data("No. of cells", "12", "", Icons.Outlined.Power)
    val minimumvoltage = Data("Minimum Voltage", "3.20 mV", " mV", Icons.Outlined.Battery1Bar)
    val maximumvoltage = Data("Maximum Voltage", "3.30 mV", " mV", Icons.Outlined.BatteryFull)
    val minimumtemp = Data("Minimum Temp", "0.5 kWh", " °C", Icons.Outlined.AcUnit)
    val maximumtemp = Data("Maximum Temp", "3.27 V", " °C", Icons.Outlined.LocalFireDepartment)
    val deltavolt = Data("Delta Voltage", "3.27 V", " mV", Icons.Outlined.BatteryFull)
    //val meanvolt = Data("Mean Temp", "3.27 V", "mV", Icons.Outlined.BatteryFull)
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