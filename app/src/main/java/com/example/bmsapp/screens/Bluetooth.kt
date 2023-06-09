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
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*


    private val bluetoothAdapter: BluetoothAdapter? by lazy { BluetoothAdapter.getDefaultAdapter() }
    private var bluetoothSocket: BluetoothSocket? = null
    private var outputStream: OutputStream? = null
    private var inputStream: InputStream? = null

    private val deviceAddress = "20:16:06:20:91:40" // Replace with your HC-05 module's address
    val receivedMessages = mutableStateListOf<String>()
    var lastmessage=mutableStateOf("no data")

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

            TextField(
                value = commandState.value,
                onValueChange = { commandState.value = it },
                label = { Text(text = "Command") },
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Button(
                onClick = { sendCommand(commandState) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Send")
            }

            //Spacer(modifier = Modifier.height(16.dp))

            for (message in receivedMessages) {
                Text(text = message)
            }
        }
    }
}
/*fun BluetoothScreen() {
    Surface(color = MaterialTheme.colors.background) {
        Column(modifier = Modifier.padding(16.dp)) {
            val commandState = remember { mutableStateOf("") }
            val connectionStatusState = remember { mutableStateOf("") }

            Text(text = connectionStatusState.value)
            Button(onClick = { connect(connectionStatusState) }) {
                Text(text = "Connect")
            }
            TextField(
                value = commandState.value,
                onValueChange = { commandState.value = it },
                label = { Text(text = "Command") }
            )
            Button(onClick = { sendCommand(commandState) }) {
                Text(text = "Send")
            }
            for (message in receivedMessages) {
                Text(text = message)
            }
        }
    }
}*/



    private fun connect(connectionStatusState: MutableState<String>) {

        GlobalScope.launch(Dispatchers.IO) {
            val device: BluetoothDevice? = bluetoothAdapter?.getRemoteDevice(deviceAddress)
            val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // Standard SPP UUID

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
            val buffer = ByteArray(1024)
            var bytes: Int

            while (true) {
                try {
                    bytes = inputStream?.read(buffer) ?: -1
                    if (bytes != -1) {
                        val receivedData = buffer.copyOf(bytes).toString(Charsets.UTF_8)
                        val messages = receivedData.split("\n", "\r")
                        // Split the received data by newline character
                        for (message in messages) {
                            val receivedMessage = message.trim() // Trim leading and trailing whitespace
                            if (receivedMessage.isNotEmpty()) {
                                Log.d("ReceivedMessage", receivedData)
                                receivedMessages.add(receivedMessage)
                                lastmessage.value=message
                            }
                        }
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    break
                }
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

    /*override fun onDestroy() {
        super.onDestroy()
        try {
            outputStream?.close()
            inputStream?.close()
            bluetoothSocket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }*/


