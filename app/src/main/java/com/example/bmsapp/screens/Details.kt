package com.example.bmsapp.screens

import DetailsRowList
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.bmsapp.ui.theme.darkgreen
import com.example.bmsapp.ui.theme.lightgreen

@Composable
fun DetailsScreen() {
    var a :String

    var percentage:Int
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.fillMaxSize()) {
        a = dropDownDetailsMenu(2)
        percentage = when (a) {
            "Bateria1" -> 10
            "Bateria2" -> 70
            "Bateria3" -> 45
            else -> 0
        }

            when (a) {
                "Bateria1" -> DetailsRowList(list =dataList3)
                "Bateria2" -> DetailsRowList(list = dataList4)
                "Bateria3" -> DetailsRowList(list = dataList5)
            }

        }
    lastmessageinterpreter(lastmessage.value)
     //Slave1.voltage.value=lastmessageSOC.value
     Slave1.minimumvoltage.value= lastmessageCellmin1voltage.value
     Slave1.maximumvoltage.value= lastmessageCellmax1voltage.value
     Slave1.maximumtemp.value= lastmessageCellmax1temp.value
     Slave1.minimumtemp.value= lastmessageCellmin1temp.value
     //Slave1.minimumtemp.value= lastmessageCellmin1temp.value
     //Slave1.maximumtemp.value= lastmessageCellmax1temp.value
     //Slave1.meanvolt.value= lastmessageCellvoltagemean1.value
     //Slave1.deltavolt.value= lastmessageCellvoltagedelta1.value

    Slave2.minimumvoltage.value= lastmessageCellmin2voltage.value
    Slave2.maximumvoltage.value= lastmessageCellmax2voltage.value
    Slave2.maximumtemp.value= lastmessageCellmax2temp.value
    Slave2.minimumtemp.value= lastmessageCellmin2temp.value
    //Slave1.minimumtemp.value= lastmessageCellmin1temp.value
    //Slave1.maximumtemp.value= lastmessageCellmax1temp.value
    //Slave2.meanvolt.value= lastmessageCellvoltagemean2.value
   // Slave2.deltavolt.value= lastmessageCellvoltagedelta2.value

    }


@Preview
@Composable
fun PreviewDetailsScreen() {
    StatusScreen()
}
val Slave1=Slave()
val Slave2=Slave()
val dataList3 = listOf(
   Slave1.cells,Slave1.minimumvoltage, Slave1.maximumvoltage,Slave1.minimumtemp,Slave1.maximumtemp
    //Slave1.meanvolt, Slave1.deltavolt
)

val dataList4 = listOf(
    Slave2.cells,Slave2.minimumvoltage, Slave2.maximumvoltage,Slave2.minimumtemp,Slave2.maximumtemp
    //Slave2.meanvolt, Slave2.deltavolt
)


val dataList5 = listOf(
    Data("No. of cells", "5", "",Icons.Outlined.Power),
    Data("Minimum Voltage", "3.20 V","", Icons.Outlined.Battery1Bar),
    Data("Maximum Voltage", "3.30 V","", Icons.Outlined.BatteryFull),
    Data("Energy", "0.46 kWh","", Icons.Outlined.Memory),
    Data("Voltage", "3.28 V","", Icons.Outlined.Bolt)
)
@Composable
fun dropDownDetailsMenu(numBatteries: Int): String {
    var expanded by remember { mutableStateOf(false) }
    val suggestions = (1..numBatteries).map { "Bateria$it" }
    var selectedText by rememberSaveable { mutableStateOf(suggestions.firstOrNull() ?: "") }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(Modifier.padding(start = 25.dp, end = 25.dp)) {
        TextField(
            value = selectedText,
            onValueChange = { selectedText = it },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                textColor = darkgreen
            ),
            modifier = Modifier
                .padding(top = 35.dp)
                .fillMaxWidth()
                .background(Color.White)
                .clickable { expanded = !expanded }
                .onGloballyPositioned { coordinates ->
                    textfieldSize = coordinates.size.toSize()
                },
            trailingIcon = {
                Icon(
                    icon,
                    "contentDescription",
                    Modifier.clickable { expanded = !expanded }
                )
            },
            readOnly = true,
            enabled = false
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
                    Text(text = label, color = lightgreen)
                }
            }
        }
    }
    return selectedText
}
