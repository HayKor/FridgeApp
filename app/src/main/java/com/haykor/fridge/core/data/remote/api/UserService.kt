package com.haykor.fridge.core.data.remote.api

import com.haykor.fridge.core.data.remote.models.CreateUserRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("users/")
    suspend fun createUser(@Body request: CreateUserRequest)
}