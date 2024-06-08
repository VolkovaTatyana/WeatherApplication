package com.mukas.weatherapp.base.mvi

interface Reducer<Action : MviAction<State>, State> {

    val initialState: State

    fun reduce(action: Action, state: State): State {
        return action.accept(state)
    }
}