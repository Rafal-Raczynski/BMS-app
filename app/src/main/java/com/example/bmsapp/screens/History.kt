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
import android.content.Context
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

//import kotlinx.coroutines.flow.internal.NoOpContinuation.context
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

object BatteryHistoryDataStorage {

    private const val PREFS_NAME = "battery_history_prefs"
    private const val KEY_BATTERY_HISTORY = "battery_history_key"

    fun saveBatteryHistoryDataList(context: Context, list: List<BatteryHistoryData>) {
        val gson = Gson()
        val jsonString = gson.toJson(list)

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_BATTERY_HISTORY, jsonString).apply()
    }

    fun getBatteryHistoryDataList(context: Context): List<BatteryHistoryData> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(KEY_BATTERY_HISTORY, null)

        return if (jsonString != null) {
            val type = object : TypeToken<List<BatteryHistoryData>>() {}.type
            val gson = Gson()
            gson.fromJson(jsonString, type)
        } else {
            emptyList()
        }
    }
}


data class BatteryHistoryData(
    val timestamp: Long,
    val voltage: Float,
    val current: Float,
    val MaxVoltage: Float,
    val MinVoltage: Float
)

//var batteryHistoryDataList = mutableListOf<BatteryHistoryData>()
val batteryHistoryDataList = mutableListOf<BatteryHistoryData>()



fun addNewBatteryHistoryData(context: Context) {
    val newTimestamp = System.currentTimeMillis()
    val newVoltage = lastmessageVoltagelist.lastOrNull()?:0f
    val newCurrent = lastmessageCurrentlist.lastOrNull()?:0f
    val newMaxVoltage = lastmessageMaxVoltagelist.lastOrNull()?:0f
    val newMinVoltage= lastmessageMinVoltagelist.lastOrNull()?:0f


    val newData = BatteryHistoryData(
        timestamp = newTimestamp,
        voltage = newVoltage,
        current = newCurrent,
        MaxVoltage = newMaxVoltage,
        MinVoltage = newMinVoltage
    )

    //batteryHistoryDataList = batteryHistoryDataList.plus(newData)
    //BatteryHistoryDataStorage.saveBatteryHistoryDataList(this, newData)
    batteryHistoryDataList.add(newData)
    BatteryHistoryDataStorage.saveBatteryHistoryDataList(context, batteryHistoryDataList)
}

@Composable
fun HistoryScreen() {
    // val batteryHistorySize = batteryHistoryDataList.size

    //lastmessageinterpreter(lastmessage.value)
    val batteryHistorySize = batteryHistoryDataList.size
    val context = LocalContext.current
    val initialDataLoaded = rememberSaveable { mutableStateOf(false) }
    val showData = remember { mutableStateOf(false) }
    val selectedData = remember { mutableStateListOf<Int>() }
    var resetButtonClicked by remember { mutableStateOf(false) }
    val chartData = createLineChartData(selectedData)
    if (!initialDataLoaded.value) {
        val initialData = BatteryHistoryDataStorage.getBatteryHistoryDataList(context)
        batteryHistoryDataList.addAll(initialData)
        initialDataLoaded.value = true
    }

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
                            //data=createLineChartData(selectedData)
                             data = chartData // Use the chartData here

                            invalidate()


                        }
                    },update = { chart ->

                    chart.data = chartData // Update the chart with new data when recomposed
                    chart.invalidate()
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
                    text = if (selectedData.contains(2)) "Remove Max Voltage" else "Toggle Max Voltage",
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
                    text = if (selectedData.contains(3)) "Remove Min Voltage" else "Toggle Min Voltage",
                    fontSize = 16.sp
                )
            }
            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .width(200.dp),
                onClick = { resetButtonClicked = true }
            ) {
                Text(text = "Reset Data", fontSize = 16.sp, color = Color.Red)
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
        if (resetButtonClicked) {
            resetData()
            resetButtonClicked = false // Reset the state to avoid re-triggering
        }
    }
    LaunchedEffect(batteryHistorySize) {
        addNewBatteryHistoryData(context)

    }
    lastmessageinterpreter(lastmessage.value)
    /*LaunchedEffect(Unit) {
          while (true) {
           delay(1000) // Czekaj 1 sekundę
            addNewBatteryHistoryData(context) // Dodaj nowe wartości
        }
    }*/
}
@Composable
fun resetData() {
    val context = LocalContext.current
    batteryHistoryDataList.clear()
    BatteryHistoryDataStorage.saveBatteryHistoryDataList(context, batteryHistoryDataList)
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
    "Max Voltage" to Color.Blue.toArgb(),
    "Min Voltage" to Color.Magenta.toArgb()
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
        lineDataSet.setDrawCircles(false)

        lineDataSets.add(lineDataSet)
    }

    return LineData(lineDataSets as List<ILineDataSet>?)
}
fun getValueForIndex(data: BatteryHistoryData, index: Int): Float {
    return when (index) {
        0 -> data.voltage
        1 -> data.current
        2 -> data.MaxVoltage
        3 -> data.MinVoltage
        else -> 0f
    }
}

fun getLabelForIndex(index: Int): String {
    return when (index) {
        0 -> "Voltage"
        1 -> "Current"
        2 -> "Max Voltage"
        3 -> "Min Voltage"
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