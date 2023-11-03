package com.example.bleframe.data.ble

import javax.inject.Inject

class BleDataSources @Inject constructor(var bleService: BleService, var bleScanner: BleScanner) {

    //todo проверка ble
    fun sjgfdj(){
        bleScanner.adapter
        bleScanner.manager
    }
}