package com.haykor.fridge.core.data.remote.api

import com.haykor.fridge.core.data.remote.models.AuthResponse
import com.haykor.fridge.core.data.remote.models.LoginRequest
import com.haykor.fridge.core.data.remote.models.Result
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest,
    ): Result<AuthResponse>


    @DELETE("auth/logout")
    suspend fun logout(
        @Header("Authorization") accessToken: String, // FIXME: potentially fix the header
    )
}

