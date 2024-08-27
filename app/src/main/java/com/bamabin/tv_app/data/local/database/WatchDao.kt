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

    @Query("DELETE FROM WatchData WHERE id=:id")
    suspend fun deleteWithId(id: Int)

    @Query("SELECT * FROM WatchData WHERE id=:id")
    suspend fun getData(id: Int): WatchData

    @Query("SELECT id FROM WatchData")
    suspend fun getAllIds(): List<Int>

    @Query("SELECT COUNT(*) FROM WatchData WHERE id!=:id")
    suspend fun getOtherCount(id: Int): Int

    @Query("SELECT id FROM WatchData ORDER BY updatedAt LIMIT 1")
    suspend fun getOldestId(): Int
}