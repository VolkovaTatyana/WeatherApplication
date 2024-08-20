package com.mukas.weatherapp.base

import androidx.lifecycle.ViewModel
import com.mukas.weatherapp.LoggerImpl
import com.mukas.weatherapp.logger.MyLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel <T: Any> : ViewModel() {

    protected val logger: MyLogger = LoggerImpl()

    protected val _state by lazy { MutableStateFlow(createInitialState()) }
    val state by lazy { _state.asStateFlow() }

    protected abstract fun createInitialState(): T

    protected fun log() {
        logger.log("*** test log from ${this.javaClass.simpleName}")
    }
}