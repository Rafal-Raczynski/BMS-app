package com.example.bmsapp.screens

import DetailsRowList
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.bmsapp.ui.theme.darkgreen
import com.example.bmsapp.ui.theme.lightgreen

@Composable
fun DetailsScreen() {
    var a :String

    var percentage:Int
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.fillMaxSize()) {
        a = dropDownDetailsMenu()
        percentage = when (a) {
            "Bateria1" -> 10
            "Bateria2" -> 70
            "Bateria3" -> 45
            else -> 0
        }
        //Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {
            //  a = dropDownMenu()
            // percentage = when (a) {
            //    "dataList" -> 10
            //   "dataList1" -> 70
            //   else -> 0
            // }
            /*Surface(
                modifier = Modifier
                    .padding(top = 35.dp)
                    .fillMaxWidth()

            ) {
                BatteryCircleProgress(
                    percentage = percentage,
                    backgroundColor = Color.LightGray,
                    strokeWidth = 15.dp
                )
            }*/
            when (a) {
                "Bateria1" -> DetailsRowList(list = dataList3)
                "Bateria2" -> DetailsRowList(list = dataList4)
                "Bateria3" -> DetailsRowList(list = dataList5)
            }
            // InfoCardsList(list = dataList)
        val value = randomFloat()
        updateDataList(dataList = dataList3)
        updateDataList(dataList = dataList4)
        updateDataList(dataList = dataList5)
        }
    }
//}


@Preview
@Composable
fun PreviewDetailsScreen() {
    StatusScreen()
}

val dataList3 = listOf(
    Data("No. of cells", "5", Icons.Outlined.Power),
    Data("Minimum Voltage", "3.20 V", Icons.Outlined.Battery1Bar),
    Data("Maximum Voltage", "3.30 V", Icons.Outlined.BatteryFull),
    Data("Energy", "21 Ah", Icons.Outlined.Memory),
    Data("Voltage", "3.27 V", Icons.Outlined.Bolt)
)

val dataList4 = listOf(
    Data("No. of cells", "5", Icons.Outlined.Power),
    Data("Minimum Voltage", "3.20 V", Icons.Outlined.Battery1Bar),
    Data("Maximum Voltage", "3.30 V", Icons.Outlined.BatteryFull),
    Data("Energy", "18 Ah", Icons.Outlined.Memory),
    Data("Voltage", "3.20 V", Icons.Outlined.Bolt),
)

val dataList5 = listOf(
    Data("No. of cells", "5", Icons.Outlined.Power),
    Data("Minimum Voltage", "3.20 V", Icons.Outlined.Battery1Bar),
    Data("Maximum Voltage", "3.30 V", Icons.Outlined.BatteryFull),
    Data("Energy", "21 Ah", Icons.Outlined.Memory),
    Data("Voltage", "3.28 V", Icons.Outlined.Bolt)
)
@Composable
fun dropDownDetailsMenu(): String {

    var expanded by remember { mutableStateOf(false) }
    val suggestions = listOf("Bateria1","Bateria2","Bateria3")
    var selectedText by rememberSaveable { mutableStateOf("Bateria1") }

    var textfieldSize by remember { mutableStateOf(Size.Zero)}

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown


    Column(Modifier.padding(start = 25.dp,end=25.dp)) {
        // Surface(
        //  modifier = Modifier
        //      .padding(top = 35.dp)
        //      .fillMaxWidth()

        //){
        TextField(
            value = selectedText,
            onValueChange = { selectedText = it },colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White, textColor = darkgreen
            ),
            modifier = Modifier
                .padding(top = 35.dp)
                .fillMaxWidth()
                .background(Color.White)

                .clickable{ expanded = !expanded }
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()


                },
            //label = {Text("Label")},
            trailingIcon = {
                Icon(icon,"contentDescription",
                    Modifier.clickable { expanded = !expanded })
            }, readOnly = true,enabled=false
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
                .requiredSizeIn(maxHeight = 200.dp)

        ) {
            suggestions.forEach { label ->
                DropdownMenuItem(onClick = {
                    selectedText = label
                    expanded = false
                }) {
                    Text(text = label,color= lightgreen)
                }
            }
        }
        // }







    }
    return selectedText
}