package ru.gb.vokruge.model.repositories.net

import okhttp3.Interceptor
import okhttp3.Response

class CacheInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request());

        val maxAge = 60 * 60
        return originalResponse.newBuilder()
            .header("Cache-Control", "public, max-age=" + maxAge)
            .build()
    }
}