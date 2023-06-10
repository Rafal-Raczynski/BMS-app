package com.example.bmsapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.coroutines.delay

data class BatteryHistoryData(
    val timestamp: Long,
    val voltage: Float,
    val current: Float,
    val temperature: Float,
    val soc: Float
)

var batteryHistoryDataList = listOf(
    BatteryHistoryData(1623210000, 12.5f, 2.3f, 30.5f, 80.0f),
    BatteryHistoryData(1623220000, 12.6f, 2.1f, 31.0f, 85.0f),
    BatteryHistoryData(1623230000, 12.4f, 2.4f, 29.8f, 77.0f),
    BatteryHistoryData(1623240000, 12.7f, 2.2f, 31.5f, 88.0f),
    BatteryHistoryData(1623250000, 12.3f, 2.5f, 29.0f, 75.0f)
)

val lineColors = listOf(
    Color.Red.toArgb(),
    Color.Green.toArgb(),
    Color.Blue.toArgb(),
    Color.Magenta.toArgb()
)

@Composable
fun HistoryScreen() {
    val showData = remember { mutableStateOf(false) }
    val selectedData = remember { mutableStateListOf<Int>() }

    Surface(color = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { showData.value = !showData.value }) {
                Text(text = if (showData.value) "Hide Data" else "Show Data")
            }
            if (showData.value) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    factory = { context ->
                        LineChart(context).apply {
                            description = Description().apply {
                                text = "Battery History"
                            }
                            data = createLineChartData(selectedData)
                        }
                    }
                )
            }
            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .width(200.dp),
                onClick = { toggleDataSelection(selectedData, 0) }
            ) {
                Text(
                    text = if (selectedData.contains(0)) "Remove Voltage" else "Toggle Voltage",
                    fontSize = 16.sp
                )
            }
            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .width(200.dp),
                onClick = { toggleDataSelection(selectedData, 1) }
            ) {
                Text(
                    text = if (selectedData.contains(1)) "Remove Current" else "Toggle Current",
                    fontSize = 16.sp
                )
            }
            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .width(200.dp),
                onClick = { toggleDataSelection(selectedData, 2) }
            ) {
                Text(
                    text = if (selectedData.contains(2)) "Remove Temperature" else "Toggle Temperature",
                    fontSize = 16.sp
                )
            }
            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .width(200.dp),
                onClick = { toggleDataSelection(selectedData, 3) }
            ) {
                Text(
                    text = if (selectedData.contains(3)) "Remove SOC" else "Toggle SOC",
                    fontSize = 16.sp
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000) // Czekaj 1 sekundę
            addNewBatteryHistoryData() // Dodaj nowe wartości
        }
    }
}

fun addNewBatteryHistoryData() {
    val lastData = batteryHistoryDataList.lastOrNull()
    val newTimestamp = lastData?.timestamp?.plus(1000) ?: System.currentTimeMillis()

    val newData = BatteryHistoryData(
        timestamp = newTimestamp,
        voltage = generateRandomFloat(10f, 15f),
        current = generateRandomFloat(1.5f, 2.5f),
        temperature = generateRandomFloat(25f, 35f),
        soc = generateRandomFloat(70f, 90f)
    )

    batteryHistoryDataList += newData
}

fun generateRandomFloat(min: Float, max: Float): Float {
    return min + (Math.random() * (max - min)).toFloat()
}

fun createLineChartData(selectedData: List<Int>): LineData {
    val entries = mutableListOf<List<Entry>>()

    if (selectedData.contains(0)) {
        val voltageEntries = batteryHistoryDataList.mapIndexed { index, data ->
            Entry(index.toFloat(), data.voltage)
        }
        entries.add(voltageEntries)
    }

    if (selectedData.contains(1)) {
        val currentEntries = batteryHistoryDataList.mapIndexed { index, data ->
            Entry(index.toFloat(), data.current)
        }
        entries.add(currentEntries)
    }

    if (selectedData.contains(2)) {
        val temperatureEntries = batteryHistoryDataList.mapIndexed { index, data ->
            Entry(index.toFloat(), data.temperature)
        }
        entries.add(temperatureEntries)
    }

    if (selectedData.contains(3)) {
        val socEntries = batteryHistoryDataList.mapIndexed { index, data ->
            Entry(index.toFloat(), data.soc)
        }
        entries.add(socEntries)
    }

    val lineDataSets = entries.mapIndexed { index, entryList ->
        val dataSet = LineDataSet(entryList, getDataSetLabel(index))
        dataSet.color = lineColors[index]
        dataSet.lineWidth = 2f
        dataSet.setDrawCircles(false)
        dataSet.setDrawValues(false)
        dataSet
    }

    val lineData = LineData(*lineDataSets.toTypedArray())
    lineData.setValueFormatter(object : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return value.toString()
        }
    })

    return lineData
}

private fun getDataSetLabel(index: Int): String {
    return when (index) {
        0 -> "Voltage"
        1 -> "Current"
        2 -> "Temperature"
        3 -> "SOC"
        else -> ""
    }
}

private fun toggleDataSelection(selectedData: MutableList<Int>, index: Int) {
    if (selectedData.contains(index)) {
        selectedData.remove(index)
    } else {
        selectedData.add(index)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HistoryScreen()
}

/*data class BatteryHistoryData(
    val timestamp: Long,
    val voltage: Float,
    val current: Float,
    val temperature: Float,
    val soc: Float
)

val batteryHistoryDataList = listOf(
    BatteryHistoryData(1623210000, 12.5f, 2.3f, 30.5f, 80.0f),
    BatteryHistoryData(1623220000, 12.6f, 2.1f, 31.0f, 85.0f),
    BatteryHistoryData(1623230000, 12.4f, 2.4f, 29.8f, 77.0f),
    BatteryHistoryData(1623240000, 12.7f, 2.2f, 31.5f, 88.0f),
    BatteryHistoryData(1623250000, 12.3f, 2.5f, 29.0f, 75.0f)
)

val lineColors = listOf(
    Color.Red.toArgb(),
    Color.Green.toArgb(),
    Color.Blue.toArgb(),
    Color.Magenta.toArgb()
)

@Composable
fun HistoryScreen() {
    val showData = remember { mutableStateOf(false) }
    val selectedData = remember { mutableStateListOf<Int>() }

    Surface(color = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { showData.value = !showData.value }) {
                Text(text = if (showData.value) "Hide Data" else "Show Data")
            }
            if (showData.value) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    factory = { context ->
                        LineChart(context).apply {
                            description = Description().apply {
                                text = "Battery History"
                            }
                            data = createLineChartData(selectedData)
                        }
                    }
                )
            }
            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .width(200.dp),
                onClick = { toggleDataSelection(selectedData, 0) }
            ) {
                Text(
                    text = if (selectedData.contains(0)) "Remove Voltage" else "Toggle Voltage",
                    fontSize = 16.sp
                )
            }
            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .width(200.dp),
                onClick = { toggleDataSelection(selectedData, 1) }
            ) {
                Text(
                    text = if (selectedData.contains(1)) "Remove Current" else "Toggle Current",
                    fontSize = 16.sp
                )
            }
            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .width(200.dp),
                onClick = { toggleDataSelection(selectedData, 2) }
            ) {
                Text(
                    text = if (selectedData.contains(2)) "Remove Temperature" else "Toggle Temperature",
                    fontSize = 16.sp
                )
            }
            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .width(200.dp),
                onClick = { toggleDataSelection(selectedData, 3) }
            ) {
                Text(
                    text = if (selectedData.contains(3)) "Remove SOC" else "Toggle SOC",
                    fontSize = 16.sp
                )
            }
        }
    }
}

fun createLineChartData(selectedData: List<Int>): LineData {
    val entries = mutableListOf<List<Entry>>()

    if (selectedData.contains(0)) {
        val voltageEntries = batteryHistoryDataList.mapIndexed { index, data ->
            Entry(index.toFloat(), data.voltage)
        }
        entries.add(voltageEntries)
    }

    if (selectedData.contains(1)) {
        val currentEntries = batteryHistoryDataList.mapIndexed { index, data ->
            Entry(index.toFloat(), data.current)
        }
        entries.add(currentEntries)
    }

    if (selectedData.contains(2)) {
        val temperatureEntries = batteryHistoryDataList.mapIndexed { index, data ->
            Entry(index.toFloat(), data.temperature)
        }
        entries.add(temperatureEntries)
    }

    if (selectedData.contains(3)) {
        val socEntries = batteryHistoryDataList.mapIndexed { index, data ->
            Entry(index.toFloat(), data.soc)
        }
        entries.add(socEntries)
    }

    val lineDataSets = entries.mapIndexed { index, entryList ->
        val dataSet = LineDataSet(entryList, getDataSetLabel(index))
        dataSet.color = lineColors[index].toArgb()
        dataSet.lineWidth = 2f
        dataSet.setDrawCircles(false)
        dataSet.setDrawValues(false)
        dataSet
    }

    val lineData = LineData(*lineDataSets.toTypedArray())
    lineData.setValueFormatter(object : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return value.toString()
        }
    })

    return lineData
}

private fun getDataSetLabel(index: Int): String {
    return when (index) {
        0 -> "Voltage"
        1 -> "Current"
        2 -> "Temperature"
        3 -> "SOC"
        else -> ""
    }
}

private fun toggleDataSelection(selectedData: MutableList<Int>, index: Int) {
    if (selectedData.contains(index)) {
        selectedData.remove(index)
    } else {
        selectedData.add(index)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HistoryScreen()
}*/