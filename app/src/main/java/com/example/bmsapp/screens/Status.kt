package com.example.bmsapp.screens

import DetailsRowListNoIcon
import android.content.Context
import android.hardware.BatteryState
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.bmsapp.screens.InfoCardsList
import com.example.bmsapp.ui.theme.darkgreen
import com.example.bmsapp.ui.theme.lightgreen
import kotlinx.coroutines.delay
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*


@Composable
fun StatusScreen() {
    var a :String
    val value= randomFloat()
    //var percentage:Int
    var percentage by remember{ mutableStateOf(10) }
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
           // var strValue = master.soc.value
           // var intValue = strValue.substring(0, strValue.indexOf(".")).toInt()
           // percentage=intValue
           // print(percentage)
        }
    }
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

