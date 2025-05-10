package com.era.photosearch.data.remote

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(private val apiKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val newRequest =
            originalRequest.newBuilder().apply {
                addHeader(
                    "Authorization",
                    apiKey
                ).build()
            }
        return chain.proceed(newRequest.build())
    }
}