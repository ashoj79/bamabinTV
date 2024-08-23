package com.bamabin.tv_app.data.local.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WatchData(
    @PrimaryKey
    val id: Int,
    val type: String,
    val quality: String,
    val qualityCode: String,
    val encoder: String,
    val time: Long,
    val season: Int = -1,
    val episode: Int = -1,
    val audioTrack: Int = -1,
    val subtitleTrack: Int = -1,
)