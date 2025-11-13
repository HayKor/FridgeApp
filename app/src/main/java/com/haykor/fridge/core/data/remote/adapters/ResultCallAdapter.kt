package com.haykor.fridge.core.data.remote.adapters

import com.haykor.fridge.core.data.remote.models.Result
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResultCallAdapter<T>(
    private val successType: Type
) : CallAdapter<T, Result<T>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<T>): Result<T> {
        return try {
            val response = call.execute()
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("HTTP ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }
}

class ResultCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        if (getRawType(returnType) != Result::class.java) return null
        check(returnType is ParameterizedType) { "Result must be parameterized" }

        val successType = getParameterUpperBound(0, returnType)
        return ResultCallAdapter<Any>(successType)
    }
}