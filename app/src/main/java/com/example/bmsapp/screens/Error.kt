package com.example.bmsapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIconDefaults.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bmsapp.ui.theme.lightgreen

//val masterError = mutableStateOf(1)
//val temperatureError = mutableStateOf(0)

data class Error(val name: String, val value: String)

// Symulacja odczytu błędów z BMS
val masterError = mutableStateOf("1")
val temperatureError = mutableStateOf("0")
val voltageError = mutableStateOf("0")
val currentError = mutableStateOf("1")
val communicationError = mutableStateOf("1")
val chargingError = mutableStateOf("1")
val dischargeError = mutableStateOf("1")
val overTemperatureError = mutableStateOf("1")
val cellMismatchError = mutableStateOf("0")
val powerLimitError = mutableStateOf("0")
val safetyError = mutableStateOf("1")
val softwareError = mutableStateOf("0")
val hardwareError = mutableStateOf("0")
val selfDischargeError = mutableStateOf("1")
val capacityLossError = mutableStateOf("1")

@Composable
fun ErrorScreen() {
    lastmessageinterpreter(lastmessage.value)
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "Błędy BMS:",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {


            //HW_compatibility_error.value=bin7[6].toString()
            //SYNC_lost_error.value=bin6[0].toString()

            //RXPDO1_lost_error.value=bin6[1].toString()

           // Max_discharge_10s_current.value=bin6[7].toString()
            //Max_s_discharge_10s_current.value=bin7[2].toString()
            //Configuration_sanity_check.value=bin7[3].toString()
            items(
                listOf(
                    Error("Master Error", mastererror.value),
                    Error("IBB voltage supply Error", IBB_voltage_supply_error.value),
                    Error("Cell delta voltage Error ", Cell_delta_voltage_error.value),
                    Error("Cell max temperature error", Cell_max_temperature_error.value),
                    Error("Cell min temperature error", Cell_min_temperature_error.value),
                    Error("Cell max voltage error", Cell_max_voltage_error.value),
                    Error("Cell min voltage error", Cell_min_voltage_error.value),
                    Error("AFE vref error", AFE_vref_error.value),
                    Error("Cell min temperature warning", Cell_min_temperature_warning.value),
                    Error("Cell min temperature warning", Cell_max_temperature_warning.value),
                    Error("Cell min charging temp error", Cell_min_charging_temp_error.value),
                    Error("Cell max charging temp error", Cell_max_charging_temp_error.value),
                    Error("No current sensor error", No_current_sensor_error.value),
                    Error("Lifetime current error", Lifetime_counter_error.value),
                    Error("Max s discharging current error", Max_s_discharging_current_error.value),
                    Error("Max s charging current error", Max_s_charging_current_error.value),
                    Error("Max discharging current error", Max_discharging_current_error.value),
                    Error("Max charging current error", Max_charging_current_error.value),
                    Error("SYNC lost error", SYNC_lost_error.value)

                ).filter { it.value=="1" }
            ) { error ->
                ErrorCard(error.name)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ErrorCard(errorName: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
        ) {
            Text(
                text = errorName.uppercase(),
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }
    }
}