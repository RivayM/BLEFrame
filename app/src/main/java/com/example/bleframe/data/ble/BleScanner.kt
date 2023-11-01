package com.example.bleframe.data.ble

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

/********************************************/
//FOR USE: 0. startScan
//         1. stopScan
//         2. foundNewDevice
/********************************************/

@Singleton
class BleScanner @Inject constructor(@ApplicationContext private val appContext: Context){

    private var scanner: BluetoothLeScanner? = null
    private var callback: BleScanCallback? = null
    private val settings: ScanSettings
    private var filters: List<ScanFilter>
    var adapter : BluetoothAdapter
    var manager : BluetoothManager? = null

    init {
        manager = appContext.getSystemService(BluetoothManager::class.java)
            ?: throw IllegalArgumentException("Device Android not available Bluetooth Adapter. BLE SCAN")
        adapter = manager?.adapter
            ?: throw IllegalArgumentException("Bluetooth Adapter = null")
        settings = buildSettings()
        filters = buildFilter()
    }

    private val _foundNewDevice = MutableStateFlow<BluetoothDevice?>(null)
    val foundNewDevice = _foundNewDevice /*.asStateFlow() */

    private fun buildSettings() =
        ScanSettings.Builder()
            .setScanMode((ScanSettings.SCAN_MODE_LOW_LATENCY))
            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
            .setNumOfMatches(ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT)
            .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
            .build()

    private fun buildFilter() = listOf(ScanFilter.Builder().build())

    inner class BleScanCallback : ScanCallback(){
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            if (result.device != null) getDevice(result.device)
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            results.forEach{ _ ->
                Log.e("MyLog"," mac : $results")
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("MyLog","failed scan $errorCode")
        }
    }

    private fun checkPermission( whatIsCheckPermission:() -> Unit):Boolean {
            return if (ActivityCompat.checkSelfPermission(
                    appContext,
                    Manifest.permission.BLUETOOTH_SCAN
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                whatIsCheckPermission()
                true
            } else false
        }

    private fun getDevice(device:BluetoothDevice) {
        val job = SupervisorJob()
        val scope = CoroutineScope(job+Dispatchers.Default)
        scope.launch {
            _foundNewDevice.emit(device)
            job.complete()
            job.join()
        }
    }

    fun startScan() {
        callback = BleScanCallback()
        scanner = adapter.bluetoothLeScanner
        checkPermission { scanner?.startScan(filters, settings, callback) }
    }

    fun stopScan() {
        checkPermission { scanner?.stopScan(callback) }
        scanner = null
        callback = null
    }
}