package com.example.bmsapp.screens

import android.hardware.BatteryState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bmsapp.R
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.unit.dp
import com.example.bmsapp.screens.InfoCardsList


@Composable
fun StatusScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier
                .padding(top = 35.dp)
                .fillMaxWidth()
        ) {
            BatteryCircleProgress(
                percentage = 10,
                fillColor = MaterialTheme.colors.primary,
                backgroundColor = Color.LightGray,
                strokeWidth = 15.dp
            )
        }
        InfoCardsList(list = dataList)
    }
}

@Preview
@Composable
fun PreviewBatteryScreen() {
    StatusScreen()
}

val dataList = listOf(
    Data("Health", "hovno", Icons.Outlined.HealthAndSafety),
    Data("Temperature", "hovno", Icons.Outlined.Thermostat),
    Data("Source", "hovno", Icons.Outlined.Cable),
    Data("Status", "hovno", Icons.Outlined.Power),
    Data("Technologie", "hovno", Icons.Outlined.Memory),
    Data("Voltage", "hovno", Icons.Outlined.Bolt),
    Data("Technologie", "hovno", Icons.Outlined.Memory),
    Data("Voltage", "hovno", Icons.Outlined.Bolt),
)