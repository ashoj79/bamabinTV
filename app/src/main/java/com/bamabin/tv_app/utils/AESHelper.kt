package com.bamabin.tv_app.utils

import android.content.Context
import android.util.Base64
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class AESHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    external fun getIV(): String
    external fun getKey(): String
    fun getPackageManager()=context.packageManager
    fun getPackageName()=context.packageName

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    fun decrypt(encryptedData: String): String {
        try {
            val keyBytes = getKey().toByteArray(Charsets.UTF_8)
            val ivBytes = getIV().toByteArray(Charsets.UTF_8)

            val secretKeySpec = SecretKeySpec(keyBytes, "AES")
            val ivParameterSpec = IvParameterSpec(ivBytes)

            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)

            val encryptedBytes = Base64.decode(encryptedData, Base64.DEFAULT)

            val originalBytes = cipher.doFinal(encryptedBytes)

            return String(originalBytes, Charsets.UTF_8)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return ""
    }
}