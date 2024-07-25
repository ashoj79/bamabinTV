package com.bamabin.tv_app.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ConnectionChecker @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun isConnect(): Boolean {
        var result = false
        val cm: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
        } else {
            cm.run {
                if (activeNetworkInfo?.type == ConnectivityManager.TYPE_WIFI ||
                    activeNetworkInfo?.type == ConnectivityManager.TYPE_MOBILE
                )
                    result = true
            }
        }
        if (result)
            result=!checkVpn()
        return result
    }

    private fun checkVpn():Boolean{
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networks: Array<Network> = cm.allNetworks

        for (i in networks.indices) {
            val caps = cm.getNetworkCapabilities(networks[i])
            if (caps!!.hasTransport(NetworkCapabilities.TRANSPORT_VPN))
                return true
        }

        return try {
            cm.getNetworkInfo(ConnectivityManager.TYPE_VPN)!!.isConnectedOrConnecting
        }catch (_:Exception){false}
    }
}