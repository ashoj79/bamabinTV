package com.bamabin.tv_app.utils

import io.socket.client.IO
import io.socket.client.Socket
import okhttp3.OkHttpClient

object SocketHelper {
    lateinit var socket: Socket

    @Synchronized
    fun connect(url: String) {
        try {
            val options = IO.Options()
            val client = OkHttpClient()
            options.callFactory = client
            options.webSocketFactory = client

            socket = IO.socket(url, options)
            socket.connect()
        }catch (_:Exception){}
    }

    @Synchronized
    fun disconnect() {
        try {
            socket.disconnect()
        }catch (_:Exception){}
    }
}