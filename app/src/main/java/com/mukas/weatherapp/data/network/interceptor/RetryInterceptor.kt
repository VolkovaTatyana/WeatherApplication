package com.mukas.weatherapp.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RetryInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        var attempt = 0
        var response: Response
        var lastException: IOException? = null

        while (attempt < MAX_RETRY) {
            try {
                response = chain.proceed(chain.request())
                if (response.isSuccessful) {
                    return response
                }
            } catch (e: IOException) {
                lastException = e
            }

            attempt++
        }

        // If we exit the loop, it means we have exhausted the retry attempts
        throw lastException ?: IOException("Unknown error")
    }

    companion object {
        private const val MAX_RETRY = 3
    }
}