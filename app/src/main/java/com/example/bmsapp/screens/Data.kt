package com.example.bmsapp.screens

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

//class Master(){
//    val voltage=Data()
//}

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