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

class BleService @Inject constructor(@ApplicationContext context: Context) : BleManager(context){

    private val NOTIFICATION_DEVICE_FLOW = MutableSharedFlow<BluetoothDevice>()
    val notificationDeviceFlow = NOTIFICATION_DEVICE_FLOW.asSharedFlow()
    private val NOTIFICATION_DATA_FLOW = MutableSharedFlow<Data>()
    val notificationDataFlow = NOTIFICATION_DATA_FLOW.asSharedFlow()
    /* INFO_SERVICE_FLOW - хранится количество поддерживаемых характеристик
     */
    private val INFO_SERVICE_FLOW = MutableSharedFlow<Int>()
    val infoServiceFlow = INFO_SERVICE_FLOW.asSharedFlow()
    /* GIVE_DATA_FLOW - можно фильтровать источник данных, с какого UUID данные были получены
     * Макс время подготовки 1 сек, после этого времени разрешается их прочитать
     */
    private val GIVE_DATA_FLOW = MutableSharedFlow<ByteArray>()
    val giveDataFlow  = GIVE_DATA_FLOW.asSharedFlow()
    /* LOG_INFO_FLOW - библиотека использует логирование всех внутренних процессов,
     * их можно получить
     */
    private val LOG_INFO_FLOW = MutableSharedFlow<String>()
    val logInfoFlow = LOG_INFO_FLOW.asSharedFlow()

    /* Для устройств энергомеры":   */
    fun readSCSD(){ readResponse(charSCSD!!) { byteArray-> emitData(byteArray) } }

    fun readNumberReadCharacteristic(numberReadCharacteristic: Int) {
        when {
            numberReadCharacteristic > listChar.size -> return
            numberReadCharacteristic <= 0 -> return
            listChar[numberReadCharacteristic] == null -> return
            else -> {
                readResponse(listChar[numberReadCharacteristic]!!){ byteArray-> emitData(byteArray) }
            }
        }
    }
    fun readWriteAndNoty(){
        readResponse(charWriteAndNoty!!) { byteArray-> emitData(byteArray) }
    }

    fun write(
        byteArray: ByteArray,
        characteristic : BluetoothGattCharacteristic? = charWriteAndNoty,
        typeWrite: Int = WRITE_TYPE_NO_RESPONSE
    ) {
        writeResponse(characteristic,byteArray, typeWrite)
    }

    /* ИНИЦИАЛИЗАЦИЯ И НАСТРОЙКА БИБЛИОТЕКИ:   */

    // Чтение из характеристики
    private fun readResponse(
        characteristic : BluetoothGattCharacteristic,
        whatDo:(ByteArray) -> Unit
    ){
        readCharacteristic(characteristic)
            .with { _, data ->
                whatDo(data.value?.clone() ?: byteArrayOf())
            }
            .enqueue()
    }

    // Запись в характеристику
    private fun writeResponse(
        characteristic : BluetoothGattCharacteristic?,
        byteArray: ByteArray,
        typeWrite: Int,
    ){
        writeCharacteristic(characteristic,byteArray, typeWrite)
            .enqueue()
    }

    // Эмитить данные полученные после чтения характеристик
    private fun emitData(byteArray: ByteArray) =
        scope.launch {
            GIVE_DATA_FLOW.emit(byteArray)
            parentJob.complete()
            parentJob.join()
        }

    // Логирование библиотеки
    override fun log(priority: Int, message: String) {
        Log.println(priority, "MyLog", "системные сообщения -> $message")
        scope.launch {
            LOG_INFO_FLOW.emit("$priority /системные сообщения -> $message")
            parentJob.complete()
            parentJob.join()
            parentJob.cancel()
        }
    }

    override fun getGattCallback(): BleManagerGattCallback {
        return GattCallbackImpl()
    }

    private inner class GattCallbackImpl : BleManagerGattCallback() {

        override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
            // здесь необходимо получить характеристики и сервисы для определения поддержки работы с устройством
            // Определение сервиса( его поиск) или если известен получить его:
            serviceGattSERVICE = gatt.getService(UUID_CHARACTERISTIC_SERVICE)
            // Подготавливаем список характеристик
            val tempList = mutableListOf<BluetoothGattCharacteristic?>()
            if (serviceGattSERVICE != null) {
                // определение характеристик по найденным uuid с помощью сервиса
                // uuid известны поэтому получим их сразу
                // заполним характеристики
                charWriteAndNoty =
                    serviceGattSERVICE!!.getCharacteristic(UUID_CHARACTERISTIC_WRITE_AND_NOTY)
                charSCSD =
                    serviceGattSERVICE!!.getCharacteristic(UUID_CHARACTERISTIC_SCSD)
                LIST_UUID_READ.forEach{
                    tempList.add(serviceGattSERVICE!!.getCharacteristic(it))
                }
            }
            // Принудительная проверека поддержки и удаление не поддерживаемых характеристик
            // Если содержится null
            listChar = tempList.filterNotNull().toMutableList()
            //отправить количество поддерживаемых характеристик
            scope.launch {
                INFO_SERVICE_FLOW.emit(listChar.size)
                parentJob.join()
                parentJob.complete()
            }
            //проверка поддержки (разорвет соединение если хотябы 1 = null)
            val check : Boolean = !(listChar.contains(null)) && listChar.isNotEmpty()
            return (charSCSD != null
                    && charWriteAndNoty != null) && check
        }

        override fun initialize() {
            // здесь необходимо установить вызовы: доступно к чтению, готов ответ
            // нотификация и индикация
            // нельзя вызывать здесь прерываемые функции!
            setNotificationCallback(charWriteAndNoty).with{ device, data ->
                scope.launch {
                    NOTIFICATION_DATA_FLOW.emit(data)
                    NOTIFICATION_DEVICE_FLOW.emit(device)
                    parentJob.join()
                    parentJob.complete()
                }
            }
            enableNotifications(charWriteAndNoty)
           }

        override fun onServicesInvalidated() {
            // обнулить (поддержку)
            charSCSD = null
            charWriteAndNoty = null
            listChar.clear()
        }
    }

    companion object {
        // Разное вспомогательное
        private val parentJob = SupervisorJob()
        private val scope = CoroutineScope(parentJob + Dispatchers.Default)

        /* для устройств энергомеры
         * дескриптор = характеристике ( 1 )
         * uuid b91bSSXX-8bef-45e2-97c3-1cd862d914df
         * Сервисы SS -> 0x0100
         * Характеристики xx -> 0x01 - 0x11(HEX!!)  (0x02-0x04 - резерв)
         * СПИСОК UUID
         */
        private val UUID_CHARACTERISTIC_SERVICE = UUID.fromString("b91b0100-8bef-45e2-97c3-1cd862d914df")
        private val UUID_CHARACTERISTIC_SCSD = UUID.fromString("b91b0101-8bef-45e2-97c3-1cd862d914df")
        private val UUID_CHARACTERISTIC_WRITE_AND_NOTY = UUID.fromString("b91b0105-8bef-45e2-97c3-1cd862d914df")
        private val UUID_CHARACTERISTIC_READ_1 = UUID.fromString("b91b0106-8bef-45e2-97c3-1cd862d914df")
        private val UUID_CHARACTERISTIC_READ_2 = UUID.fromString("b91b0107-8bef-45e2-97c3-1cd862d914df")
        private val UUID_CHARACTERISTIC_READ_3 = UUID.fromString("b91b0108-8bef-45e2-97c3-1cd862d914df")
        private val UUID_CHARACTERISTIC_READ_4 = UUID.fromString("b91b0109-8bef-45e2-97c3-1cd862d914df")
        private val UUID_CHARACTERISTIC_READ_5 = UUID.fromString("b91b010A-8bef-45e2-97c3-1cd862d914df")
        private val UUID_CHARACTERISTIC_READ_6 = UUID.fromString("b91b010B-8bef-45e2-97c3-1cd862d914df")
        private val UUID_CHARACTERISTIC_READ_7 = UUID.fromString("b91b010C-8bef-45e2-97c3-1cd862d914df")
        private val UUID_CHARACTERISTIC_READ_8 = UUID.fromString("b91b010D-8bef-45e2-97c3-1cd862d914df")
        private val UUID_CHARACTERISTIC_READ_9 = UUID.fromString("b91b010E-8bef-45e2-97c3-1cd862d914df")
        private val UUID_CHARACTERISTIC_READ_10 = UUID.fromString("b91b010F-8bef-45e2-97c3-1cd862d914df")
        private val UUID_CHARACTERISTIC_READ_11 = UUID.fromString("b91b0110-8bef-45e2-97c3-1cd862d914df")
        private val UUID_CHARACTERISTIC_READ_12 = UUID.fromString("b91b0111-8bef-45e2-97c3-1cd862d914df")
        /* СПИСОК ХАРАКТЕРИСТИК
         * char = characteristic
         * Первые 3 характеристики 100% - должны быть поддерживаемые
         * остальные незакомментированные могут не поддерживаться
         */
        private var serviceGattSERVICE: BluetoothGattService? = null
        private var charSCSD: BluetoothGattCharacteristic? = null
        private var charWriteAndNoty: BluetoothGattCharacteristic? = null
        /* Будут в listChar, когда пройдут проверку
        private var charRead1: BluetoothGattCharacteristic? = null
        private var charRead2: BluetoothGattCharacteristic? = null
        private var charRead3: BluetoothGattCharacteristic? = null
        private var charRead4: BluetoothGattCharacteristic? = null
        private var charRead5: BluetoothGattCharacteristic? = null
        private var charRead6: BluetoothGattCharacteristic? = null
        private var charRead7: BluetoothGattCharacteristic? = null
        private var charRead8: BluetoothGattCharacteristic? = null
        private var charRead9: BluetoothGattCharacteristic? = null
        private var charRead10: BluetoothGattCharacteristic? = null
        private var charRead11: BluetoothGattCharacteristic? = null
        private var charRead12: BluetoothGattCharacteristic? = null
        */
        private var listChar = mutableListOf<BluetoothGattCharacteristic?>()
        /* СПИСОК UUID - отсутвие поддержки которых - допустимо
         */
        private val LIST_UUID_READ = listOf<UUID>(
            UUID_CHARACTERISTIC_READ_1,
            UUID_CHARACTERISTIC_READ_2,
            UUID_CHARACTERISTIC_READ_3,
            UUID_CHARACTERISTIC_READ_4,
            UUID_CHARACTERISTIC_READ_5,
            UUID_CHARACTERISTIC_READ_6,
            UUID_CHARACTERISTIC_READ_7,
            UUID_CHARACTERISTIC_READ_8,
            UUID_CHARACTERISTIC_READ_9,
            UUID_CHARACTERISTIC_READ_10,
            UUID_CHARACTERISTIC_READ_11,
            UUID_CHARACTERISTIC_READ_12,
        )
    }
}
