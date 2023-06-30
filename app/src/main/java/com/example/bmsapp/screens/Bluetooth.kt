package com.example.bmsapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/*@Composable
fun BluetoothScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Magenta),
        contentAlignment = Alignment.Center
    ) {
       Text(
            text = "BLUETOOTH",
            fontSize = MaterialTheme.typography.h3.fontSize,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

    }

}

@Composable
@Preview
fun BluetoothScreenPreview() {
   BluetoothScreen()
}*/
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.bmsapp.ui.theme.lightgreen
import com.example.bmsapp.ui.theme.taupe100
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.util.*


private val bluetoothAdapter: BluetoothAdapter? by lazy { BluetoothAdapter.getDefaultAdapter() }
private var bluetoothSocket: BluetoothSocket? = null
private var outputStream: OutputStream? = null
private var inputStream: InputStream? = null
private val deviceAddress = "20:16:06:20:91:40" // Replace with your HC-05 module's address
val receivedMessages = mutableStateListOf<String>()

var lastmessage = mutableStateOf("no data")
var lastmessageVoltage = mutableStateOf("no data")
var lastmessageCurrent = mutableStateOf("no data")
var lastmessageSOC = mutableStateOf("no data")
var lastmessagePower = mutableStateOf("no data")
var lastmessageVoltagelist = mutableListOf<Float>()
var lastmessageCurrentlist = mutableListOf<Float>()
var lastmessageTemperaturelist = mutableListOf<Float>()
var lastmessageSoclist = mutableListOf<Float>()


@Composable
fun BluetoothScreen() {
    Surface(color = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val connectionStatusState = remember { mutableStateOf("") }
            val commandState = remember { mutableStateOf("") }

            Text(
                text = connectionStatusState.value,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(Color.Blue)
            ) {
                Button(
                    onClick = { connect(connectionStatusState) },
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(text = "Connect", color = Color.White)
                }
            }



                Text(text = lastmessage.value)

        }

    }
}

private fun connect(connectionStatusState: MutableState<String>) {
    GlobalScope.launch(Dispatchers.IO) {
        val device: BluetoothDevice? = bluetoothAdapter?.getRemoteDevice(deviceAddress)
        val uuid: UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // Standard SPP UUID

        try {
            bluetoothSocket = device?.createRfcommSocketToServiceRecord(uuid)
            bluetoothSocket?.connect()
            outputStream = bluetoothSocket?.outputStream
            inputStream = bluetoothSocket?.inputStream

            connectionStatusState.value = "Connected to $deviceAddress"

            startReceivingMessages()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

private fun startReceivingMessages() {
    GlobalScope.launch(Dispatchers.IO) {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8))

            while (true) {
                val receivedMessage = reader.readLine() ?: break
                val trimmedMessage = receivedMessage.trim()

                /*if (trimmedMessage.isNotEmpty()) {
                    //Log.d("ReceivedMessage", trimmedMessage)
                    receivedMessages.add(trimmedMessage)
                    lastmessage.value = trimmedMessage
                }*/
                if (trimmedMessage.length == 19 && trimmedMessage.matches(Regex("[0-9A-Fa-f]+")))
                {
                    lastmessage.value=trimmedMessage
                    val id = trimmedMessage.substring(0, 3)
                    val hex1 = trimmedMessage.substring(3, 5)
                    val hex2 = trimmedMessage.substring(5, 7)
                    val hex3 = trimmedMessage.substring(7, 9)
                    val hex4 = trimmedMessage.substring(9, 11)
                    val hex5 = trimmedMessage.substring(11, 13)
                    val hex6 = trimmedMessage.substring(13, 15)
                    val hex7 = trimmedMessage.substring(15, 17)
                    val hex8 = trimmedMessage.substring(17, 19)

                    if(id=="190")
                    {
                        val voltagehex=hex8+hex7
                        val currenthex=hex6+hex5
                        val SOChex=hex4
                        val voltagefloat=voltagehex.toInt(16)*0.0625
                        val currentfloat=currenthex.toInt(16)*0.0625-2048
                        val powerfloat=voltagefloat*currentfloat
                        val SOCInt=SOChex.toInt(16)
                        val SOCfloat=SOChex.toInt(16).toFloat()*1.0
                        lastmessageVoltage.value=String.format("%.4f",voltagefloat)
                        lastmessageCurrent.value=String.format("%.4f",currentfloat)
                        lastmessageSOC.value=SOCInt.toString()
                        lastmessagePower.value= String.format("%.4f", powerfloat)
                        lastmessageVoltagelist.add(voltagefloat.toFloat())
                        lastmessageCurrentlist.add(currentfloat.toFloat())
                        lastmessageSoclist.add(SOCfloat.toFloat())



                    }

                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            reader?.close()
        }
    }
}


private fun sendCommand(commandState: MutableState<String>) {
    GlobalScope.launch(Dispatchers.IO) {
        val command = commandState.value
        command.plus("\n") // Append a newline character

        try {
            outputStream?.write(command.toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
