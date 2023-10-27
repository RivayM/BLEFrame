package com.example.bleframe.domain.use_case_ble

import android.util.Log
import com.example.bleframe.data.ble.BleScanner
import javax.inject.Inject

class UseCaseBleGetListDeviceToConnect @Inject constructor(
    private val scanner: BleScanner
) {

    val userData = scanner.foundDevice

    fun startScan(permission:Boolean){
        Log.d("MyLog"," стартую ")
        scanner.startScan(permission)
    }

    fun stopScan(permission:Boolean){
        Log.d("MyLog"," заканчиваю ")
        scanner.stopScan(permission)
    }
}