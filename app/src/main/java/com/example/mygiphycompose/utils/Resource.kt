package com.example.mygiphycompose.utils

sealed class Resource<T>(
    val data: T? = null,
    val error: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(error: String?, data: T? = null) : Resource<T>(data, error)
    class Loading<T> : Resource<T>()
    class Empty<T> : Resource<T>()

}