package com.bamabin.tv_app.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bamabin.tv_app.data.local.database.model.WatchedEpisode

@Dao
interface WatchedEpisodeDao {
    @Insert
    suspend fun insert(watchedEpisode: WatchedEpisode)

    @Query("SELECT * FROM WatchedEpisode WHERE id=:id")
    suspend fun getWatchedEpisodes(id: Int): List<WatchedEpisode>

    @Query("DELETE FROM WatchedEpisode WHERE id=:id AND season=:season AND episode=:episode")
    suspend fun delete(id: Int, season: Int, episode: Int)

    @Query("DELETE FROM WatchedEpisode")
    suspend fun deleteAll()

    @Query("SELECT * FROM WatchedEpisode ORDER BY time LIMIT 1")
    suspend fun getOldest(): WatchedEpisode

    @Query("SELECT COUNT(*) FROM WatchedEpisode WHERE id=:id OR season!=:season OR episode!=:episode")
    suspend fun getOtherCount(id: Int, season: Int, episode: Int): Int
}