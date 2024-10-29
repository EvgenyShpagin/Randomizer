package com.random.randomizer.domain.common

sealed class Result<out D, out E : Error> {
    data class Success<out D>(val data: D) : Result<D, Nothing>()
    data class Failure<out E : Error>(val error: E) : Result<Nothing, E>()

    inline fun onSuccess(action: (data: D) -> Unit): Result<D, E> {
        if (this is Success) {
            action(data)
        }
        return this
    }

    inline fun onFailure(action: (error: E) -> Unit): Result<D, E> {
        if (this is Failure) {
            action(error)
        }
        return this
    }
}