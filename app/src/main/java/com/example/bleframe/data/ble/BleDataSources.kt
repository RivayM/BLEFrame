package com.example.bleframe.data.ble

import android.bluetooth.BluetoothDevice
import no.nordicsemi.android.ble.observer.ConnectionObserver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BleDataSources @Inject constructor(private var bleService: BleService, private var bleManagerApp: BleManagerApp) {

    val listDevice = bleManagerApp.foundDevice
    val logInfoFlow = bleService.logInfoFlow

    fun startScan() = bleManagerApp.startScan()
    fun stopScan()  = bleManagerApp.stopScan()
    fun getBleState() = bleManagerApp.getBleState()

    fun connect(device:BluetoothDevice) = bleService.connect(device)
            .retry(RETRY_COUNT,RETRY_DELAY)
            .useAutoConnect(false)
            .done{ bleService.connectionObserver = connectionObserver }
            .fail{_,status-> }
            .enqueue()


    fun disconnect() = bleService.disconnect()

    companion object{
        const val RETRY_COUNT = 100
        const val RETRY_DELAY = 2
        private val connectionObserver = object : ConnectionObserver {
            override fun onDeviceConnecting(device: BluetoothDevice) {
            }

            override fun onDeviceConnected(device: BluetoothDevice) {
            }

            override fun onDeviceFailedToConnect(device: BluetoothDevice, reason: Int) {
            }

            override fun onDeviceReady(device: BluetoothDevice) {
            }

            override fun onDeviceDisconnecting(device: BluetoothDevice) {
            }

            override fun onDeviceDisconnected(device: BluetoothDevice, reason: Int) {
            }

        }
    }
}