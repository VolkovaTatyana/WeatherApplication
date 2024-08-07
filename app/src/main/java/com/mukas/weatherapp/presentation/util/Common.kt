package com.mukas.weatherapp.presentation.util

import com.mukas.weatherapp.presentation.theme.CardGradients
import com.mukas.weatherapp.presentation.theme.Gradient

fun getGradientByIndex(index: Int): Gradient {
    val gradients = CardGradients.gradients
    return gradients[index % gradients.size]
}