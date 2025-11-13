package com.haykor.fridge.core.data.remote.util

object CookieUtil {
    fun createRefreshTokenCookie(refreshToken: String): String {
        return "refresh_token=$refreshToken"
    }
}