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
class BleManagerApp @Inject constructor(@ApplicationContext private val appContext: Context){

    private var scanner: BluetoothLeScanner? = null
    private var callback: BleScanCallback? = null
    private val settings: ScanSettings
    private var filters: List<ScanFilter>
    private var adapter : BluetoothAdapter?
    private var manager : BluetoothManager? = null
    private val _foundDevice = MutableSharedFlow<BluetoothDevice?>()
    val foundDevice = _foundDevice

    init {
        manager = appContext.getSystemService(BluetoothManager::class.java)
        adapter = manager?.adapter
        settings = buildSettings()
        filters = buildFilter()
    }


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

    private fun checkPermission(whatToDoIfCheckPermissionTrue:() -> Unit):Boolean {
            return if (ActivityCompat.checkSelfPermission(
                    appContext,
                    Manifest.permission.BLUETOOTH_SCAN
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                whatToDoIfCheckPermissionTrue()
                true
            } else false
        }

    private fun getDevice(device:BluetoothDevice) {
        val job = SupervisorJob()
        val scope = CoroutineScope(job+Dispatchers.Default)
        scope.launch {
            _foundDevice.emit(device)
            job.complete()
            job.join()
        }
    }

    fun startScan() {
        callback = BleScanCallback()
        scanner = adapter?.bluetoothLeScanner
        checkPermission { scanner?.startScan(filters, settings, callback) }
    }

    fun stopScan() {
        checkPermission { scanner?.stopScan(callback) }
        scanner = null
        callback = null
    }

    fun getBleState() = adapter?.isEnabled

}