package com.example.bleframe.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bleframe.entities.Person

@Entity(tableName = "localStoragePersons")
data class RoomDBOPerson(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    override var id: Int? = null,

    @ColumnInfo(name = "first_name")
    override var firstName: String,

    @ColumnInfo(name = "last_name")
    override var lastName: String,

    @ColumnInfo(name = "email")
    override var email: String,

    @ColumnInfo(name = "password")
    override var password: String,   // *закодирован  // из базы всегда не null

    @ColumnInfo(name = "authorized_app")
    override var authorizedApp: Boolean
): Person.PersonDataAuth
