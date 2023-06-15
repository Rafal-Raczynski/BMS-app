package com.example.bmsapp.screens

import DetailsRowListNoIcon
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*


@Composable
fun StatusScreen() {
    var a :String
    //val value= randomFloat()
    //var percentage:Int
    val percentage by remember{ mutableStateOf(10) }
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()/*.verticalScroll(scrollState)*/) {
            DetailsRowListNoIcon(list = dataListIntro)
            Surface(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()

            )
            {
                BatteryCircleProgress(
                    percentage = percentage,
                    backgroundColor = Color.LightGray,
                    strokeWidth = 15.dp
                )
            }
            InfoCardsList(list = dataList)
        }
    }
    master.voltage.value= lastmessage.value
}


@Preview
@Composable
fun PreviewBatteryScreen() {
    StatusScreen()
}

val master=Master()
val dataList = listOf(
    master.status,master.voltage,master.current,master.energy,master.temperature,master.sof)


val dataListIntro = listOf(
    Data("Total number of cells", "15", Icons.Outlined.Power),
    Data("Nominal capacity", "60 Ah", Icons.Outlined.Power))

@Composable
fun updateDataList(dataList: List<Data>) {
    val value = randomFloat()
    dataList.map { data ->
        data.updateValue(value)
        data
    }
}

