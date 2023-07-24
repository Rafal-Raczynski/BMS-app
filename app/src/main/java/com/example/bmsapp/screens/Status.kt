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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


@Composable
fun StatusScreen() {
    var a :String
    var percentage by remember{ mutableStateOf(master.soc.value.toIntOrNull()?:5) }
    LaunchedEffect(lastmessageSOC.value) {
        percentage = lastmessageSOC.value.toIntOrNull() ?: percentage
   }
    //var percentage:Int

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()/*.verticalScroll(scrollState)*/) {
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
            InfoCardsList(list = dataList)
        }
    }

   // Thread.sleep(500)
    //generateString()
    lastmessageinterpreter(lastmessage.value)
    master.voltage.value= lastmessageVoltage.value
    master.current.value= lastmessageCurrent.value
    master.soc.value= lastmessageSOC.value
    master.power.value= lastmessagePower.value
    master.sof.value= mastererror.value
    master.status.value= lastmessageStatus.value
    master.cellvolt.value= lastmessagecellvoltage.value
    master.celltemp.value= lastmessagecelltemp.value
   // master.soc.value= lastmessage.value
}


@Preview
@Composable
fun PreviewBatteryScreen() {
    StatusScreen()
}

val master=Master()
val dataList = listOf(
    master.status,master.voltage,master.current,master.power,master.cellvolt,master.celltemp)


val dataListIntro = listOf(
    Data("Total number of cells", "15","", Icons.Outlined.Power),
    Data("Nominal capacity", "60"," Ah" ,Icons.Outlined.Power))

@Composable
fun updateDataList(dataList: List<Data>) {
    val value = randomFloat()
    dataList.map { data ->
        data.updateValue(value)
        data
    }
}
fun lastmessageinterpreter(trimmedMessage:String)
{
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

fun getRandomHexDigit(): Char {
    val hexDigits = "0123456789ABCDEF"
    return hexDigits[Random.nextInt(hexDigits.length)]
}
fun generateString(){
        val prefix = "410"
        val randomChars = (1..16).map { getRandomHexDigit() }.joinToString("")
        lastmessage.value = prefix + randomChars
}