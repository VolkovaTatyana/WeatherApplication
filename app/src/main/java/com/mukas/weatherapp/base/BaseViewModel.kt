package com.mukas.weatherapp.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel <T: Any> : ViewModel() {

    protected val _state by lazy { MutableStateFlow(createInitialState()) }
    val state by lazy { _state.asStateFlow() }

    protected abstract fun createInitialState(): T
}