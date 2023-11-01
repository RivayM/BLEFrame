package com.example.bleframe.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bleframe.entities.BleDeviceApp

@Entity(tableName = "localStorageDevice")
data class RoomDBO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    override var id: Int? = null,

    @ColumnInfo(name = "id_device")
    override var idDevice: Int? = null,

    @ColumnInfo(name = "name")
    override var name: String? = null,
): BleDeviceApp
