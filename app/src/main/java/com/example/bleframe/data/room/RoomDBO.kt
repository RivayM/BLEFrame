package com.example.bleframe.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "localStorageDevice")
data class RoomDBO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "id_device")
    var idDevice: Int? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,
)
