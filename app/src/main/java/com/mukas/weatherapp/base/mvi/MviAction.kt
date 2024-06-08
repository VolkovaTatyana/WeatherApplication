package com.mukas.weatherapp.base.mvi

interface MviAction<State> {

    fun accept(state: State): State {
        return state
    }
}