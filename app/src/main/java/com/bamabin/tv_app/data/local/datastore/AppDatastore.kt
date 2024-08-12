package com.bamabin.tv_app.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AppDatastore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object{
        private val Context.datastore by preferencesDataStore("bamabin")
        private val baseUrlKey = stringPreferencesKey("base_url")
        private val tokenKey = stringPreferencesKey("token")
    }

    suspend fun setBaseUrl(data: String) {
        context.datastore.edit { it[baseUrlKey] = data }
    }

    suspend fun getBaseUrl(): String {
        val preferences = context.datastore.data.first()
        return preferences[baseUrlKey] ?: ""
    }

    suspend fun setToken(data: String) {
        context.datastore.edit { it[tokenKey] = data }
    }

    suspend fun getToken(): String {
        val preferences = context.datastore.data.first()
        return preferences[tokenKey] ?: ""
    }
}