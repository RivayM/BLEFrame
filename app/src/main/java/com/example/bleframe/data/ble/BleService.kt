package com.example.bleframe.data.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattCharacteristic.*
import android.bluetooth.BluetoothGattService
import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import no.nordicsemi.android.ble.BleManager
import no.nordicsemi.android.ble.data.Data
import java.util.*
import javax.inject.Inject

class BleService @Inject constructor(@ApplicationContext appContext: Context) : BleManager(appContext){

    val notificationDeviceFlow = _notificationDeviceFlow.asSharedFlow()
    val notificationDataFlow = _notificationDataFlow.asSharedFlow()
    val infoServiceFlow = _infoServiceFlow.asSharedFlow()
    val dataFlow  = _dataFlow.asSharedFlow()
    val logInfoFlow = _logInfoFlow.asSharedFlow()

    override fun onPairingRequestReceived(device: BluetoothDevice, variant: Int, key: Int) {
        super.onPairingRequestReceived(device, variant, key)
        //todo get key for pairing and write key
    }

    override fun log(priority: Int, message: String) {
        Log.println(priority, "MyLog", "System message -> $message")
    }

    @Deprecated("Deprecated in Java", ReplaceWith("GattCallbackImpl()"))
    override fun getGattCallback(): BleManagerGattCallback {
        return GattCallbackImpl()
    }

    private inner class GattCallbackImpl : BleManagerGattCallback() {

        @Deprecated("Deprecated in Java")
        override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
            serviceGattSERVICE = gatt.getService(UUID_CHARACTERISTIC_SERVICE) ?: return false
            return (charSCSD != null && characteristicWR != null)
        }

        @Deprecated("Deprecated in Java")
        override fun initialize() {
            setNotificationCallback(characteristicWR).with{ device, data ->
                scope.launch {
                    _notificationDataFlow.emit(data)
                    _notificationDeviceFlow.emit(device)
                    parentJob.complete()
                    parentJob.join()
                }
            }
            enableNotifications(characteristicWR)
           }

        @Deprecated("Deprecated in Java")
        override fun onServicesInvalidated() {
            charSCSD = null
            characteristicWR = null
        }
    }

    fun write(
        characteristic : BluetoothGattCharacteristic? = characteristicWR,
        byteArray: ByteArray
    ){
        writeCharacteristic(characteristic, byteArray, WRITE_TYPE_NO_RESPONSE).enqueue()
    }

    fun read(
        characteristic : BluetoothGattCharacteristic? = characteristicWR
    ){
        readCharacteristic(characteristic).enqueue()
    }

    companion object {
        private val parentJob = SupervisorJob()
        private val scope = CoroutineScope(parentJob + Dispatchers.Default)

        private val _notificationDeviceFlow = MutableSharedFlow<BluetoothDevice>()
        private val _notificationDataFlow = MutableSharedFlow<Data>()
        private val _infoServiceFlow = MutableSharedFlow<Int>()
        private val _dataFlow = MutableSharedFlow<ByteArray>()
        private val _logInfoFlow = MutableSharedFlow<String>()

        private val UUID_CHARACTERISTIC_SERVICE = UUID.fromString("b91b0100-8bef-45e2-97c3-1cd862d914df")
        private val UUID_CHARACTERISTIC_READ_1 = UUID.fromString("b91b0106-8bef-45e2-97c3-1cd862d914df")

        private var serviceGattSERVICE: BluetoothGattService? = null
        private var charSCSD: BluetoothGattCharacteristic? = null
        private var characteristicWR: BluetoothGattCharacteristic? = null
    }
}
