package com.example.bleframe.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RoomDAO {

    @Query("SELECT * FROM localStoragePersons WHERE " +
            "first_name LIKE :firstName " +
            "AND " +
            "last_name LIKE :lastName " +
            "AND " +
            "email LIKE :email")
    suspend fun signIn(firstName:String,lastName:String,email:String):List<RoomDBOPerson>

    @Query("SELECT * FROM localStoragePersons WHERE " +
            "first_name LIKE :firstName " +
            "AND " +
            "password LIKE :password")
    suspend fun logIn(firstName:String,password:String):List<RoomDBOPerson>

    @Query("SELECT * FROM localStoragePersons WHERE " +
            "authorized_app LIKE :authorized")
    suspend fun foundAuthorized(authorized:Boolean):List<RoomDBOPerson>

    @Insert
    suspend fun insert(data: RoomDBOPerson)

    //мейби insert со стратегией ( но там много условий ) наверное лучше update
    // в этом моменте пока что так ( ну и логика соотвествующая)
    @Update
    suspend fun update(data: RoomDBOPerson)

}
