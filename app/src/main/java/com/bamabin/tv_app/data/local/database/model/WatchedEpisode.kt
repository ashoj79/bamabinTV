package com.bamabin.tv_app.data.local.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WatchedEpisode(
    @PrimaryKey(autoGenerate = true) val pk: Int = 0,
    val id: Int,
    val season: Int,
    val episode: Int,
    val time: Long = 0
)
