import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bmsapp.screens.Data
import com.example.bmsapp.ui.theme.lightgreen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailsRowList(list: List<Data>) {
    LazyColumn(
        //columns = GridCells.Fixed(3),
        modifier = Modifier
            .padding(10.dp)
        //.height(600.dp)
        //.fillMaxHeight()


    ) {
        itemsIndexed(items = list) { _, item ->
            RowItem(item)
        }
    }
}

@Composable
fun RowItem(data: Data) {
    Card(
        elevation = 3.dp,
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .padding(0.dp)
            .fillMaxSize()
        //, backgroundColor = Color.White tu zmieniamy kolor tła karty


    ) {

        Row(
            modifier = Modifier
                .padding(vertical = 0.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically


        ) {

            Row(horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically){
            Image(

                imageVector = data.icon,
                contentDescription = data.label,
                colorFilter = ColorFilter.tint(lightgreen),
                modifier = Modifier
                    .size(50.dp, 50.dp)
                    //.size(20.dp, 20.dp)
                    .fillMaxWidth()
                //.align()
            )


            Text(
                text = data.label,
                color = Color.Gray,
                //fontSize = 12.sp,
                fontSize = 15.sp,
                // modifier = Modifier.align(Alignment.CenterHorizontally)
            )}

            Text(
                text = data.value,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.DarkGray,
                //fontSize = 18.sp,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                //modifier = Modifier.align(Alignment.CenterHorizontally)
            )

        }
    }

}

val dataLists = listOf(
    Data("Health", "Good", Icons.Outlined.HealthAndSafety),
    Data("Temperature", "10C", Icons.Outlined.Thermostat),
    Data("Source", "10V", Icons.Outlined.Cable),
    Data("Status", "5", Icons.Outlined.Power),
    Data("Energy", "1,5mAh", Icons.Outlined.Memory),
    Data("Voltage", "5V", Icons.Outlined.Bolt),
    Data("Technologie", "siuu", Icons.Outlined.Memory),
    Data("Voltage", "66", Icons.Outlined.Bolt), Data("Status", "hovno", Icons.Outlined.Power),
    Data("Technologie", "aaa", Icons.Outlined.Memory),
    Data("Voltage", "hovno", Icons.Outlined.Bolt),
    Data("Technologie", "aaa", Icons.Outlined.Memory)
)
