package com.example.bleframe.data.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

class BleScanner @Inject constructor(@ApplicationContext private val context: Context){
    /*
     * 2 Активных метода startScan и stopScan
     * foundDevice - список устройтсв
     * Остальное иниициализация сканера
     */

    private var manager : BluetoothManager? = null
    private var scanner: BluetoothLeScanner? = null
    private var callback: BleScanCallback? = null
    private val settings: ScanSettings
    private var filters: List<ScanFilter>        // фильтр по поиску устройств

    init {
        settings = buildSettings()
        filters = buildFilter()
    }

    private val _foundDevice = MutableSharedFlow<BluetoothDevice>()
    val foundDevice = _foundDevice.asSharedFlow()

    private fun getDevice(device:BluetoothDevice) {
        val job = SupervisorJob()
        val scope = CoroutineScope(job+Dispatchers.Default)
        scope.launch {
            _foundDevice.emit(device)
            job.complete()
            job.join()
        }
    }

    @SuppressLint("MissingPermission")
    // Просьба сделать проверку на получение разрешения
    // Если не выполнить команда scanner не выполнится
    // ошибку можно игнорировать
    fun startScan(permission : Boolean){
        manager = context.getSystemService(BluetoothManager::class.java)
        if (callback == null){            // оповещение сканера
            callback = BleScanCallback()
            if (manager?.adapter == null) // По фатку проверка есть ли у устройства андроид блютуз
                throw IllegalArgumentException("Device Android not available Bluetooth Adapter. BLE SCAN")
            else{
                scanner = manager?.adapter?.bluetoothLeScanner
                if(permission)
                    scanner?.startScan(filters, settings, callback) // требует разрешения
                else
                    throw IllegalArgumentException("permission not received. BLE SCAN")
            }
        }
    }

    @SuppressLint("MissingPermission")
    // Просьба сделать проверку на получение разрешения
    // Если не выполнить команда scanner не выполнится
    // ошибку можно игнорировать
    fun stopScan(permission : Boolean){
        if (callback != null){
            if(permission)  scanner?.stopScan(callback) // требует разрешения
            else throw IllegalArgumentException("permission not received. BLE SCAN")
            scanner = null
            callback = null
        }
        manager = null
    }

    /* Первичная настройка сканера */
    private fun buildSettings() =
        ScanSettings.Builder()
            .setScanMode((ScanSettings.SCAN_MODE_LOW_LATENCY))          // режим работы приемника(потребление)
            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)    // вызов колбека в соответсвии с фильтрами( срабатывает каждый раз
            .setNumOfMatches(ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT)  // количество пакетов ( кол-во данных необходимых для совпадения)
            .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)           // режим поиска(сопр(даже со слабым сигналом)
            .build()

    /* Список установленных фильтров для поиска устройств*/
    private fun buildFilter() =
        listOf(
            ScanFilter.Builder()
            // фильтр ( null = все ближайщие устройства )
            .build()
        )

    /* Внутренний класс об оповещении, в зависимости от настроек! читать Scanner Bluetooth*/
    inner class BleScanCallback : ScanCallback(){
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            if (result.device != null) getDevice(result.device)
            //Log.d("MyLog"," mac : ${result.device.address}")
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            results.forEach{ _ ->
                Log.d("MyLog"," mac : ${results}")
                // получение готовым списком найденных устройств
                // сработает если использовать другой setScanMode((ScanSettings.SCAN_MODE_LOW_LATENCY)
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.d("MyLog","failed scan $errorCode")
        }
    }
}