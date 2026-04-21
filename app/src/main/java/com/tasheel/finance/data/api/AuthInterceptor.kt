package com.tasheel.finance.data.api

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor() : Interceptor {
    @Volatile var token: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().let { original ->
            token?.let { original.newBuilder().header("Authorization", "Bearer $it").build() } ?: original
        }
        return chain.proceed(request)
    }
}
