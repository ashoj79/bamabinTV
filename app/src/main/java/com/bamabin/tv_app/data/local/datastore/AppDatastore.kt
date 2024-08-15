package com.bamabin.tv_app.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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
        private val avatarKey = stringPreferencesKey("avatar")
        private val usernameKey = stringPreferencesKey("username")
        private val emailKey = stringPreferencesKey("email")
        private val subtitleTextColorKey = intPreferencesKey("sub_text_color")
        private val subtitleBgColorKey = intPreferencesKey("sub_bg_color")
        private val subtitleFontKey = intPreferencesKey("sub_font")
        private val subtitleSizeKey = intPreferencesKey("sub_size")
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

    suspend fun setAvatar(data: String) {
        context.datastore.edit { it[avatarKey] = data }
    }

    suspend fun getAvatar(): String {
        val preferences = context.datastore.data.first()
        return preferences[avatarKey] ?: ""
    }

    suspend fun setUsername(data: String) {
        context.datastore.edit { it[usernameKey] = data }
    }

    suspend fun getUsername(): String {
        val preferences = context.datastore.data.first()
        return preferences[usernameKey] ?: ""
    }

    suspend fun setEmail(data: String) {
        context.datastore.edit { it[emailKey] = data }
    }

    suspend fun getEmail(): String {
        val preferences = context.datastore.data.first()
        return preferences[emailKey] ?: ""
    }

    suspend fun setSubTextColor(data: Int) {
        context.datastore.edit { it[subtitleTextColorKey] = data }
    }

    suspend fun getSubTextColor(): Int {
        val preferences = context.datastore.data.first()
        return preferences[subtitleTextColorKey] ?: 0
    }

    suspend fun setSubBgColor(data: Int) {
        context.datastore.edit { it[subtitleBgColorKey] = data }
    }

    suspend fun getSubBgColor(): Int {
        val preferences = context.datastore.data.first()
        return preferences[subtitleBgColorKey] ?: 0
    }

    suspend fun setSubFont(data: Int) {
        context.datastore.edit { it[subtitleFontKey] = data }
    }

    suspend fun getSubFont(): Int {
        val preferences = context.datastore.data.first()
        return preferences[subtitleFontKey] ?: 0
    }

    suspend fun setSubSize(data: Int) {
        context.datastore.edit { it[subtitleSizeKey] = data }
    }

    suspend fun getSubSize(): Int {
        val preferences = context.datastore.data.first()
        return preferences[subtitleSizeKey] ?: 1
    }
}