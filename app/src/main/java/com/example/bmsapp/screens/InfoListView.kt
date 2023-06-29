package com.example.bmsapp.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import com.example.bmsapp.ui.theme.lightgreen


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InfoCardsList(list: List<Data>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(10.dp)
            .height(600.dp)
            //.fillMaxHeight()


    ) {
        itemsIndexed(items = list) { _, item ->
            InfoItem(item)
        }
    }
}

@Composable
fun InfoItem(data: Data) {
    var Kolor=Color.White
    if(data.label=="Voltage" && data.value<"15")
    {
        Kolor=Color.Red
    }
    Card(
        elevation = 3.dp,
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()
            , backgroundColor = Kolor //tu zmieniamy kolor tła karty


    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 20.dp)
                .fillMaxSize()


        ) {
            Image(
                imageVector = data.icon,
                contentDescription = data.label,
                colorFilter = ColorFilter.tint(lightgreen),
                modifier = Modifier
                    .size(50.dp, 50.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )


            Text(
                text = data.label,
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(
                text = data.value+data.unit,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.DarkGray,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

        }
    }

}

val dataLists = listOf(
    Data("Health", "Good","", Icons.Outlined.HealthAndSafety),
    Data("Temperature", "10C","", Icons.Outlined.Thermostat),
    Data("Source", "10V","", Icons.Outlined.Cable),
    Data("Status", "5","", Icons.Outlined.Power),
    Data("Energy", "1,5mAh","", Icons.Outlined.Memory),
    Data("Voltage", "5V","", Icons.Outlined.Bolt),
    Data("Technologie", "siuu","", Icons.Outlined.Memory))



@Preview
@Composable
fun InfoItemPreview() {
    InfoCardsList(dataLists)
}