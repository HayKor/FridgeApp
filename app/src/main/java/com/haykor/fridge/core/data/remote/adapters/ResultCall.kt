package com.haykor.fridge.core.data.remote.adapters

import com.haykor.fridge.core.data.remote.models.Result
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

internal class ResultCall<T>(proxy: Call<T>) : CallDelegate<T, Result<T>>(proxy) {

    override fun enqueueImpl(callback: Callback<Result<T>>) {
        proxy.enqueue(ResultCallback(this, callback))
    }

    override fun cloneImpl(): ResultCall<T> {
        return ResultCall(proxy.clone())
    }

    private class ResultCallback<T>(
        private val proxy: ResultCall<T>,
        private val callback: Callback<Result<T>>
    ) : Callback<T> {

        override fun onResponse(call: Call<T>, response: Response<T>) {
            val result = if (response.isSuccessful) {
                Result.Success(response.body() as T)
            } else {
                Result.Error("Error: ${response.code()} ${response.message()}")
            }
            callback.onResponse(proxy, Response.success(result))
        }

        override fun onFailure(call: Call<T>, error: Throwable) {
            val result = when (error) {
                is retrofit2.HttpException -> {
                    Result.Error("Error: ${error.code()} ${error.message()}")
                }

                is IOException -> Result.Error(error.toString())
                else -> Result.Error(error.toString())
            }

            callback.onResponse(proxy, Response.success(result))
        }
    }

    override fun timeout(): Timeout {
        return proxy.timeout()
    }
}