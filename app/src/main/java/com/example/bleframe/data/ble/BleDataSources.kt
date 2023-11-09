package com.example.bleframe.data.ble

import javax.inject.Inject

class BleDataSources @Inject constructor(var bleService: BleService, var bleManagerApp: BleManagerApp) {

    fun startScan() = bleManagerApp.startScan()
    fun stopScan()  = bleManagerApp.stopScan()
    fun getBleState() = bleManagerApp.getBleState()



}