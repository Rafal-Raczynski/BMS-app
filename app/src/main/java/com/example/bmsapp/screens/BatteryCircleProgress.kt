package com.example.bmsapp.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.OfflineBolt
import androidx.compose.material.icons.rounded.OfflineBolt
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import com.example.bmsapp.ui.theme.*

@Composable
fun BatteryCircleProgress(
    modifier: Modifier = Modifier,
    percentage: Int,
    backgroundColor: Color,
    strokeWidth: Dp
) {
    var fillColor=lightgreen
    if(percentage in 11..39) {
        fillColor = yellow
    }
    else if(percentage<=10){
        fillColor = red
    }
    Box (modifier=modifier.background(color= taupe100)){
        Canvas(
            modifier = modifier
                .size(180.dp)
                .padding(10.dp)
                .align(Alignment.Center)
        ) {
            // Background Line
            drawArc(
                color = backgroundColor,
                140f,
                260f,
                false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round),
                size = Size(size.width, size.height)
            )

            drawArc(
                color = fillColor,
                140f,
                percentage * 2.60f,
                false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round),
                size = Size(size.width, size.height)
            )


            val angleInDegrees = (percentage * 2.60) + 50.0
            val radius = (size.height / 2)
            val x =
                -(radius * kotlin.math.sin(Math.toRadians(angleInDegrees))).toFloat() + (size.width / 2)
            val y = (radius * cos(Math.toRadians(angleInDegrees))).toFloat() + (size.height / 2)

            drawCircle(
                color = Color.White,
                radius = 10f,
                center = Offset(x, y)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 20.dp)
        ) {
            Text(
                text = "Remaining",
                color = Color.Gray,
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(
                text = "$percentage%",
                color = Color.DarkGray,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

        }
    }


}

@Preview
@Composable
fun PreviewProgressBar() {
    BatteryCircleProgress(
        percentage = 10,
        backgroundColor = Color.DarkGray,
        strokeWidth = 10.dp
    )
}