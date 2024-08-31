package com.bamabin.tv_app.di

import android.content.Context
import androidx.room.Room
import com.bamabin.tv_app.data.local.database.BamabinDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    fun provideBamabinDB(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, BamabinDB::class.java, "bamabin_db").build()

    @Provides
    fun provideWatchDao(db: BamabinDB) = db.getWatchDao()

    @Provides
    fun provideWatchedEpisodeDao(db: BamabinDB) = db.getWatchedEpisodeDa()
}