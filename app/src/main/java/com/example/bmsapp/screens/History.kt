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
import com.github.mikephil.charting.components.Legend
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

//var batteryHistoryDataList = mutableListOf<BatteryHistoryData>()
val batteryHistoryDataList = mutableListOf<BatteryHistoryData>()



fun addNewBatteryHistoryData() {
    val newTimestamp = System.currentTimeMillis()
    val newVoltage = lastmessageVoltagelist.lastOrNull()?:0f
    val newCurrent = lastmessageCurrentlist.lastOrNull()?:0f
    val newTemperature = lastmessageTemperaturelist.lastOrNull()?:0f
    val newSoc = lastmessageSoclist.lastOrNull()?:0f


        val newData = BatteryHistoryData(
            timestamp = newTimestamp,
            voltage = newVoltage,
            current = newCurrent,
            temperature = newTemperature,
            soc = newSoc
        )

        //batteryHistoryDataList = batteryHistoryDataList.plus(newData)
        batteryHistoryDataList.add(newData)

}

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
                            // Utwórz legendę
                            val legend = legend
                            legend.isEnabled = true
                            legend.textSize = 12f
                            legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                            legend.setDrawInside(false)

                            // Utwórz dane wykresu
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
            /*Button(
                modifier = Modifier
                    .padding(8.dp)
                    .width(200.dp),
                onClick = { addNewRandomVoltage() }
            ) {
                Text(text = "Add Random Voltage", fontSize = 16.sp)
            }*/
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000) // Czekaj 1 sekundę
            addNewBatteryHistoryData() // Dodaj nowe wartości
        }
    }
}



fun toggleDataSelection(selectedData: SnapshotStateList<Int>, index: Int) {
    if (selectedData.contains(index)) {
        selectedData.remove(index)
    } else {
        selectedData.add(index)
    }
}

val lineColors = mapOf(
    "Voltage" to Color.Red.toArgb(),
    "Current" to Color.Green.toArgb(),
    "Temperature" to Color.Blue.toArgb(),
    "SOC" to Color.Magenta.toArgb()
)

fun createLineChartData(selectedData: List<Int>): LineData {
    val entries = mutableListOf<List<Entry>>()

    for (index in selectedData) {
        val dataEntries = batteryHistoryDataList.mapIndexed { i, data ->
            Entry(i.toFloat(), getValueForIndex(data, index))
        }
        entries.add(dataEntries)
    }

    val lineDataSets = mutableListOf<LineDataSet>()

    for ((index, dataEntries) in entries.withIndex()) {
        val label = getLabelForIndex(selectedData[index])
        val lineDataSet = LineDataSet(dataEntries, label)
        lineDataSet.color =
            (lineColors[label] ?: Color.Black) as Int  // Ustawienie koloru na podstawie mapy lineColors

        // Pozostała konfiguracja datasetu
        lineDataSet.lineWidth = 2f
        lineDataSet.circleRadius = 4f
        lineDataSet.valueTextSize = 10f
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawCircles(true)

        lineDataSets.add(lineDataSet)
    }

    return LineData(lineDataSets as List<ILineDataSet>?)
}
fun getValueForIndex(data: BatteryHistoryData, index: Int): Float {
    return when (index) {
        0 -> data.voltage
        1 -> data.current
        2 -> data.temperature
        3 -> data.soc
        else -> 0f
    }
}

fun getLabelForIndex(index: Int): String {
    return when (index) {
        0 -> "Voltage"
        1 -> "Current"
        2 -> "Temperature"
        3 -> "SOC"
        else -> ""
    }
}



@Preview
@Composable
fun PreviewHistoryScreen() {
    HistoryScreen()
}
fun addNewRandomVoltage() {
    val min=10.0f
    val max=15.0f
    val randomVoltage = (min + (Math.random() * (max - min))).toFloat()
    //lastmessageVoltagelist.add(randomVoltage)
    //lastmessage.value=randomVoltage.toString()
}

/*fun main() {
    lastmessageVoltage.add(12.5f)
    lastmessageCurrent.add(2.3f)
    lastmessageTemperature.add(30.5f)
    lastmessageSoc.add(80.0f)

    // Uruchom komponent ekranu historii
    PreviewHistoryScreen()
}*/