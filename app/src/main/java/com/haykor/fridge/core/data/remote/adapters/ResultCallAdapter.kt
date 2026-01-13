package com.haykor.fridge.core.data.remote.adapters

import com.haykor.fridge.core.data.remote.models.Result
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResultCallAdapterFactory(
    private val coroutineScope: CoroutineScope,
) : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): CallAdapter<*, *>? {
        val rawReturnType = getRawType(returnType)
        when (rawReturnType) {
            Call::class.java -> {
                val callInnerType = getParameterUpperBound(0, returnType as ParameterizedType)
                val rawType = getRawType(callInnerType)
                if (rawType != Result::class.java) {
                    return null
                }

                val resultInnerType = getParameterUpperBound(0, callInnerType as ParameterizedType)
                val paramInnerType = getRawType(resultInnerType)
                return ResultCallAdapter(
                    resultType = resultInnerType,
                    paramType = paramInnerType,
                    coroutineScope = coroutineScope,
                )
            }

            else -> return null
        }
    }
}

internal class ResultCallAdapter(
    private val resultType: Type,
    private val paramType: Type,
    private val coroutineScope: CoroutineScope,
) : CallAdapter<Type, Call<Result<Type?>>> {

    override fun responseType(): Type = resultType

    override fun adapt(call: Call<Type>): Call<Result<Type?>> {
        return ResultCall(call, paramType, coroutineScope)
    }
}
