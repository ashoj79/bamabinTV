package com.bamabin.tv_app.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.bamabin.tv_app.data.local.database.model.WatchData

@Dao
interface WatchDao {
    @Upsert
    suspend fun saveOrUpdate(data: WatchData)

    @Delete
    suspend fun delete(data: WatchData)

    @Query("DELETE FROM WatchData")
    suspend fun deleteAll()

    @Query("SELECT * FROM WatchData WHERE id=:id")
    suspend fun getData(id: Int): WatchData
}