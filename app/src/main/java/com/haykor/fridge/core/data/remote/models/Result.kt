package com.haykor.fridge.core.data.remote.models

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val msg: String) : Result<Nothing>()
}