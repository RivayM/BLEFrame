package com.example.bleframe.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [RoomDBOPerson::class], version = 1
)
abstract class RoomDataBaseApp: RoomDatabase() {
    abstract fun getDataDao() : RoomDAO
}

