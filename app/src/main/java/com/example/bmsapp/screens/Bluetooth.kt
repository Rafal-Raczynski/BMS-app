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
import androidx.compose.runtime.LaunchedEffect
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

var lastmessage = mutableStateOf("410190101014510001080")
//var lastmessage = mutableStateOf("no data")
//190
var lastmessageVoltage = mutableStateOf("no data")
var lastmessageCurrent = mutableStateOf("no data")
var lastmessageSOC = mutableStateOf("no data")
var lastmessagePower = mutableStateOf("no data")
val lastmessageRemainingEnergy= mutableStateOf("no data")
var lastmessageVoltagelist = mutableListOf<Float>()
var lastmessageCurrentlist = mutableListOf<Float>()
var lastmessageTemperaturelist = mutableListOf<Float>()
var lastmessageSoclist = mutableListOf<Float>()
val IBB_voltage_supply_error=mutableStateOf("0")
val Cell_delta_voltage_error=mutableStateOf("0")
val Cell_max_temperature_error=mutableStateOf("0")
val Cell_min_temperature_error=mutableStateOf("0")
val Cell_max_voltage_error=mutableStateOf("0")
val Cell_min_voltage_error=mutableStateOf("0")
val AFE_vref_error=mutableStateOf("0")
val mastererror=mutableStateOf("0")

//290
var lastmessageBlockmin=mutableStateOf("no data")
var lastmessageStringmin=mutableStateOf("no data")
var lastmessageCellmin=mutableStateOf("no data")
var lastmessageCellmin1voltage=mutableStateOf("no data")
var lastmessageCellmin2voltage=mutableStateOf("no data")
var lastmessageCellvoltagemean1=mutableStateOf("no data")
var lastmessageCellvoltagemean2=mutableStateOf("no data")
var lastmessageCellvoltagemean=mutableStateOf("no data")
var lastmessageBalancingtempmax=mutableStateOf("no data")
//310
var lastmessagecellvoltage=mutableStateOf("no data")
var lastmessagecelltemp=mutableStateOf("no data")
//390
var lastmessageBlockmax=mutableStateOf("no data")
var lastmessageStringmax=mutableStateOf("no data")
var lastmessageCellmax=mutableStateOf("no data")
var lastmessageCellmax1voltage=mutableStateOf("no data")
var lastmessageCellmax2voltage=mutableStateOf("no data")
var lastmessageCellvoltagedelta1=mutableStateOf("no data")
var lastmessageCellvoltagedelta2=mutableStateOf("no data")
var lastmessageCellvoltagedelta=mutableStateOf("no data")
var lastmessageAFEtempmax=mutableStateOf("no data")
//410
var lastmessageBlockTmax=mutableStateOf("no data")
var lastmessageStringTmax=mutableStateOf("no data")
var lastmessageSensorTmax=mutableStateOf("no data")
var lastmessageCellmax1temp=mutableStateOf("no data")
var lastmessageCellmax2temp=mutableStateOf("no data")
var lastmessagetemperaturedelta=mutableStateOf("no data")
var lastmessageStatus=mutableStateOf("no data")
val Cell_min_temperature_warning=mutableStateOf("0")
val Cell_max_temperature_warning=mutableStateOf("0")
val Cell_min_charging_temp_error=mutableStateOf("0")
val Cell_max_charging_temp_error=mutableStateOf("0")
val HW_compatibility_error=mutableStateOf("0")
val SYNC_lost_error=mutableStateOf("0")
val Lifetime_counter_error=mutableStateOf("0")
val RXPDO1_lost_error=mutableStateOf("0")
val No_current_sensor_error=mutableStateOf("0")
val Max_discharge_10s_current=mutableStateOf("0")
val Max_s_discharge_10s_current=mutableStateOf("0")
val Max_s_discharging_current_error=mutableStateOf("0")
val Max_s_charging_current_error=mutableStateOf("0")
val Max_discharging_current_error=mutableStateOf("0")
val Max_charging_current_error=mutableStateOf("0")
val Configuration_sanity_check=mutableStateOf("0")




//490
var lastmessageBlockTmin=mutableStateOf("no data")
var lastmessageStringTmin=mutableStateOf("no data")
var lastmessageSensorTmin=mutableStateOf("no data")
var lastmessageCellmin1temp=mutableStateOf("no data")
var lastmessageCellmin2temp=mutableStateOf("no data")
var lastmessagetemperaturemean=mutableStateOf("no data")




@Composable
fun BluetoothScreen() {


    Surface(color = MaterialTheme.colors.background) {
       // LaunchedEffect(lastmessage.value ) {
       //     generateString()
       // }
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
        while(true) {
           Thread.sleep(500)
           // generateString()
           /* lastmessage.value="4101901010145100010"
            Thread.sleep(500)
            lastmessage.value="190a5020000000000ba"
            Thread.sleep(500)
            lastmessage.value="390ffff010110ffff1b"
            Thread.sleep(500)
            lastmessage.value="2900000010107d85b1a"
            Thread.sleep(500)
            lastmessage.value="31061000a0218d89a00"
            Thread.sleep(500)
            lastmessage.value="490d4010103f0b09600"
            Thread.sleep(500)*/
            // generateString()
            lastmessage.value="410d40101050c2d9100"
            Thread.sleep(500)
            lastmessage.value="190cd03ffff000007ba"
            Thread.sleep(500)
            lastmessage.value="390ffff010110ffff1b"
            Thread.sleep(500)
            lastmessage.value="290000001010700841a"
            Thread.sleep(500)
            lastmessage.value="3101000ffff19030100"
            Thread.sleep(500)
            lastmessage.value="490d40101050c2d9100"
        }
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
                        val voltagehex=hex1+hex2
                        val currenthex=hex3+hex4
                        val SOChex=hex5
                        val remainingenergyhex= hex6+hex7
                        val voltagefloat=voltagehex.toInt(16)*0.0625
                        val currentfloat=currenthex.toInt(16)*0.0625-2048
                        val remainingenergyfloat= remainingenergyhex.toInt(16)
                        val powerfloat=voltagefloat*currentfloat
                        val SOCInt=SOChex.toInt(16)
                        val SOCfloat=SOChex.toInt(16).toFloat()*1.0
                        lastmessageVoltage.value=String.format("%.4f",voltagefloat)
                        lastmessageCurrent.value=String.format("%.4f",currentfloat)
                        lastmessageSOC.value=SOCInt.toString()
                        lastmessagePower.value= String.format("%.4f", powerfloat)
                        lastmessageRemainingEnergy.value=remainingenergyfloat.toString()
                        lastmessageVoltagelist.add(voltagefloat.toFloat())
                        lastmessageCurrentlist.add(currentfloat.toFloat())
                        lastmessageSoclist.add(SOCfloat.toFloat())
                        //errors
                        val bin8= hexToBinary(hex8)
                        mastererror.value= bin8[0].toString()
                        IBB_voltage_supply_error.value=bin8[7].toString()
                        Cell_delta_voltage_error.value=bin8[6].toString()
                        Cell_max_temperature_error.value=bin8[5].toString()
                        Cell_min_temperature_error.value=bin8[4].toString()
                        Cell_max_voltage_error.value=bin8[3].toString()
                        Cell_min_voltage_error.value=bin8[2].toString()
                        AFE_vref_error.value=bin8[1].toString()



                    }
                    if(id=="290")
                    {
                        val blockminhex=hex4
                        val blockminint=blockminhex.toInt(16)
                        val cellminvoltagehex= hex1+hex2
                        val cellminvoltagefloat=cellminvoltagehex.toInt(16)*0.1
                        val cellvoltagemeanhex= hex6+hex7
                        val cellvoltagemeanfloat=cellvoltagemeanhex.toInt(16)*0.1
                        val balancingtempmaxhex=hex8
                        val balancingtempmaxfloat=balancingtempmaxhex.toInt(16)-128
                        val stringminhex=hex5
                        val cellminhex=hex3
                        val stringminint=stringminhex.toInt(16)
                        val cellminint=cellminhex.toInt(16)
                        lastmessageStringmin.value=stringminint.toString()
                        lastmessageCellmin.value=cellminint.toString()
                        lastmessageBlockmin.value=blockminint.toString()
                        lastmessageCellvoltagemean.value=String.format("%.4f", cellvoltagemeanfloat)
                        lastmessageBalancingtempmax.value=balancingtempmaxfloat.toString()
                        if (lastmessageBlockmin.value=="1")
                        {
                            lastmessageCellmin1voltage.value= String.format("%.4f", cellminvoltagefloat)
                            //lastmessageCellvoltagemean1.value=String.format("%.4f", cellvoltagemeanfloat)
                        }
                        else if (lastmessageBlockmin.value=="2")
                        {
                            lastmessageCellmin2voltage.value= String.format("%.4f", cellminvoltagefloat)
                           // lastmessageCellvoltagemean2.value=String.format("%.4f", cellvoltagemeanfloat)
                        }
                    }
                    if(id=="310")
                    {
                        val cellvoltagehex= hex3+hex4
                        val cellvoltagefloat=cellvoltagehex.toInt(16)*0.1
                        lastmessagecellvoltage.value=String.format("%.4f", cellvoltagefloat)

                        val celltemphex= hex5
                        val celltempfloat=celltemphex.toInt(16)
                        lastmessagecelltemp.value=celltempfloat.toString()
                    }
                    if(id=="390")
                    {
                        val blockmaxhex=hex4
                        val blockmaxint=blockmaxhex.toInt(16)
                        val cellmaxvoltagehex= hex1+hex2
                        val cellmaxvoltagefloat=cellmaxvoltagehex.toInt(16)*0.1
                        val cellvoltagedeltahex= hex6+hex7
                        val cellvoltagedeltafloat=cellvoltagedeltahex.toInt(16)*0.1
                        val afetempmaxhex=hex8
                        val afetempmaxfloat=afetempmaxhex.toInt(16)-128
                        val stringmaxhex=hex5
                        val cellmaxhex=hex3
                        val stringmaxint=stringmaxhex.toInt(16)
                        val cellmaxint=cellmaxhex.toInt(16)
                        lastmessageStringmax.value=stringmaxint.toString()
                        lastmessageCellmax.value=cellmaxint.toString()

                        lastmessageBlockmax.value=blockmaxint.toString()
                        lastmessageCellvoltagedelta.value=String.format("%.4f", cellvoltagedeltafloat)
                        lastmessageAFEtempmax.value=afetempmaxfloat.toString()
                        if (lastmessageBlockmax.value=="1")
                        {
                            lastmessageCellmax1voltage.value= String.format("%.4f", cellmaxvoltagefloat)
                            //lastmessageCellvoltagedelta1.value=String.format("%.4f", cellvoltagedeltafloat)
                        }
                        else if (lastmessageBlockmax.value=="2")
                        {
                            lastmessageCellmax2voltage.value= String.format("%.4f", cellmaxvoltagefloat)
                            //lastmessageCellvoltagedelta2.value=String.format("%.4f", cellvoltagedeltafloat)
                        }
                    }

                    if(id=="410")
                    {
                        val blockmaxhex=hex3
                        val blockmaxint=blockmaxhex.toInt(16)
                        val cellmaxtemphex= hex1
                        val cellmaxtempfloat=cellmaxtemphex.toInt(16)*1-128
                        val temperaturedeltahex=hex5
                        val temperaturedeltaint=temperaturedeltahex.toInt(16)-128

                        lastmessagetemperaturedelta.value=temperaturedeltaint.toString()
                        lastmessageBlockTmax.value=blockmaxint.toString()
                        val stringmaxhex=hex2
                        val sensormaxhex=hex4
                        val stringmaxint=stringmaxhex.toInt(16)
                        val sensormaxint=sensormaxhex.toInt(16)
                        lastmessageStringTmax.value=stringmaxint.toString()
                        lastmessageSensorTmax.value=sensormaxint.toString()
                        if (lastmessageBlockTmax.value=="1")
                        {
                            lastmessageCellmax1temp.value= cellmaxtempfloat.toString()
                            //lastmessageCellvoltagedelta1.value=String.format("%.4f", cellvoltagedeltafloat)
                        }
                        else if (lastmessageBlockTmax.value=="2")
                        {
                            lastmessageCellmax2temp.value= cellmaxtempfloat.toString()
                            //lastmessageCellvoltagedelta2.value=String.format("%.4f", cellvoltagedeltafloat)
                        }
                        //status
                        val bin8= hexToBinary(hex8)
                        val bin7= hexToBinary(hex7)
                        val bin6= hexToBinary(hex6)
                        var status=""
                        if(bin8[1].toString()=="1")
                        {
                            status = "RTD "
                        }
                        if(bin8[2].toString()=="1")
                        {
                            status += "RTC "
                        }
                        if(bin8[3].toString()=="1")
                        {
                            status += "FD "
                        }
                        if(bin8[4].toString()=="1")
                        {
                            status += "FC "
                        }
                        lastmessageStatus.value=status
                        //error

                        Cell_min_temperature_warning.value=bin8[5].toString()
                        Cell_max_temperature_warning.value=bin8[6].toString()
                        Cell_min_charging_temp_error.value=bin8[0].toString()
                        Cell_max_charging_temp_error.value=bin7[7].toString()
                        HW_compatibility_error.value=bin7[6].toString()
                        SYNC_lost_error.value=bin6[0].toString()
                        Lifetime_counter_error.value=bin6[2].toString()
                        RXPDO1_lost_error.value=bin6[1].toString()
                        No_current_sensor_error.value=bin6[3].toString()
                        Max_discharge_10s_current.value=bin6[7].toString()
                        Max_s_discharge_10s_current.value=bin7[2].toString()
                        Max_s_discharging_current_error.value=bin7[1].toString()
                        Max_s_charging_current_error.value=bin7[0].toString()
                        Max_discharging_current_error.value=bin6[6].toString()
                        Max_charging_current_error.value=bin6[5].toString()
                        Configuration_sanity_check.value=bin7[3].toString()



                    }

                    if(id=="490")
                    {
                        val blockminhex=hex3
                        val blockminint=blockminhex.toInt(16)
                        val cellmintemphex= hex1
                        val cellmintempfloat=cellmintemphex.toInt(16)*1-128
                        val temperaturemeanhex=hex5
                        val temperaturemeanint=temperaturemeanhex.toInt(16)-128
                        lastmessagetemperaturemean.value=temperaturemeanint.toString()
                        lastmessageBlockTmin.value=blockminint.toString()
                        val stringminhex=hex2
                        val sensorminhex=hex4
                        val stringminint=stringminhex.toInt(16)
                        val sensorminint=sensorminhex.toInt(16)
                        lastmessageStringTmin.value=stringminint.toString()
                        lastmessageSensorTmin.value=sensorminint.toString()
                        if (lastmessageBlockTmin.value=="1")
                        {
                            lastmessageCellmin1temp.value= cellmintempfloat.toString()
                            //lastmessageCellvoltagedelta1.value=String.format("%.4f", cellvoltagedeltafloat)
                        }
                        else if (lastmessageBlockTmin.value=="2")
                        {
                            lastmessageCellmin2temp.value= cellmintempfloat.toString()
                            //lastmessageCellvoltagedelta2.value=String.format("%.4f", cellvoltagedeltafloat)
                        }
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

fun hexToBinary(hexString: String): String {
    val hexChars = "0123456789ABCDEF"
    val binaryChars = arrayOf(
        "0000", "0001", "0010", "0011",
        "0100", "0101", "0110", "0111",
        "1000", "1001", "1010", "1011",
        "1100", "1101", "1110", "1111"
    )

    val binaryStringBuilder = StringBuilder()

    for (char in hexString) {
        val hexChar = char.toUpperCase()
        val hexValue = hexChars.indexOf(hexChar)

        if (hexValue in 0..15) {
            binaryStringBuilder.append(binaryChars[hexValue])
        } else {
            throw IllegalArgumentException("Invalid hexadecimal input: $hexChar")
        }
    }

    return binaryStringBuilder.toString()
}