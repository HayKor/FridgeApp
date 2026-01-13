package com.haykor.fridge.core.data.remote.adapters

import com.haykor.fridge.core.data.remote.models.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.awaitResponse
import java.lang.reflect.Type

internal class ResultCall<T : Any>(
    private val proxy: Call<T>,
    private val paramType: Type,
    private val coroutineScope: CoroutineScope,
) : Call<Result<T?>> {

    override fun enqueue(callback: Callback<Result<T?>>) {
        coroutineScope.launch {
            try {
                val response = proxy.awaitResponse()
                val result = response.toResult(paramType)
                callback.onResponse(this@ResultCall, Response.success(result))
            } catch (e: Exception) {
                val result = Result.Error(e.toString())
                callback.onResponse(this@ResultCall, Response.success(result))
            }
        }
    }

    override fun execute(): Response<Result<T?>> =
        runBlocking(coroutineScope.coroutineContext) {
            val result = proxy.execute().toResult(paramType)
            Response.success(result)
        }

    override fun clone(): Call<Result<T?>> = ResultCall(proxy.clone(), paramType, coroutineScope)
    override fun request(): Request = proxy.request()
    override fun timeout(): Timeout = proxy.timeout()
    override fun isExecuted(): Boolean = proxy.isExecuted
    override fun isCanceled(): Boolean = proxy.isCanceled
    override fun cancel() = proxy.cancel()
}

@Suppress("UNCHECKED_CAST")
internal fun <T> Response<T>.toResult(paramType: Type): Result<T?> {
    return if (isSuccessful) {
        if (paramType == Unit::class.java) {
            Result.Success(Unit as T)
        } else {
            Result.Success(body())
        }
    } else {
        throw HttpException(this)
    }
}