package com.mukas.weatherapp

import android.util.Log
import com.mukas.weatherapp.logger.MyLogger

class LoggerImpl : MyLogger {

    override fun log(message: String) {
        Log.d("Dev", "Dev: $message")
    }
}