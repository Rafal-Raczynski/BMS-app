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
        //a = dropDownMenu()
       /*when (a) {
            "Bateria1" -> 85
            else -> 0
        }*/
        Column(modifier = Modifier.fillMaxSize()/*.verticalScroll(scrollState)*/) {
            //  a = dropDownMenu()
            // percentage = when (a) {
            //    "dataList" -> 10
            //   "dataList1" -> 70
            //   else -> 0
            // }
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
            /*when (a) {
                "Bateria1" -> InfoCardsList(list = dataList)
            }*/
            InfoCardsList(list = dataList)
            //dataList.forEach(Data.updateValue())
            //val value= randomFloat()
             //dataList.map { data ->
              //  data.updateValue(value)
             //   data
            //}

           // updateDataList(dataList = dataList)
            //var strValue = master.soc.value
            //var intValue = strValue.substring(0, strValue.indexOf(".")).toInt()
           // percentage=intValue
           // print(percentage)
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
    master.status,master.voltage,master.current,master.energy,master.temperature,master.sof,master.soc)


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

