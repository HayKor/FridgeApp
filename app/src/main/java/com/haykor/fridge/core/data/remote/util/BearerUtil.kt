package com.haykor.fridge.core.data.remote.util

object BearerUtil {
    fun createBearerAccessTokenHeader(accessToken: String): String {
        return "Bearer $accessToken"
    }
}
