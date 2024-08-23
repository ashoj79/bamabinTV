package com.bamabin.tv_app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bamabin.tv_app.data.local.database.model.WatchData

@Database(entities = [WatchData::class], version = 1, exportSchema = false)
abstract class BamabinDB: RoomDatabase(){
    abstract fun getWatchDao(): WatchDao
}