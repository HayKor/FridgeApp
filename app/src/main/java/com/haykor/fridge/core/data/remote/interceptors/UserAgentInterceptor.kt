package com.haykor.fridge.core.data.remote.interceptors

import com.haykor.fridge.core.data.remote.util.UserAgentUtil
import okhttp3.Interceptor
import okhttp3.Response

class UserAgentInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request
            .newBuilder()
            .header("User-Agent", UserAgentUtil.getUserAgent())
            .build()
        return chain.proceed(newRequest)
    }
}