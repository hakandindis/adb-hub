package com.github.hakandindis.plugins.adbhub.core.result

sealed class AdbHubResult<out T> {

    data class Success<T>(val data: T) : AdbHubResult<T>()
    data class Failure(val error: AdbHubError) : AdbHubResult<Nothing>()

    val isSuccess: Boolean get() = this is Success
    val isFailure: Boolean get() = this is Failure

    fun getOrNull(): T? = (this as? Success)?.data
    fun errorOrNull(): AdbHubError? = (this as? Failure)?.error

    inline fun fold(
        onSuccess: (T) -> Unit,
        onFailure: (AdbHubError) -> Unit
    ) {
        when (this) {
            is Success -> onSuccess(data)
            is Failure -> onFailure(error)
        }
    }

    inline fun <R> map(transform: (T) -> R): AdbHubResult<R> = when (this) {
        is Success -> Success(transform(data))
        is Failure -> this
    }

    inline fun <R> mapCatching(transform: (T) -> R): AdbHubResult<R> = when (this) {
        is Success -> runCatching { transform(data) }.fold(
            onSuccess = { Success(it) },
            onFailure = { Failure(AdbHubError.Unknown(it.message ?: "Unknown error", it)) }
        )

        is Failure -> this
    }

    inline fun <R> flatMap(transform: (T) -> AdbHubResult<R>): AdbHubResult<R> = when (this) {
        is Success -> transform(data)
        is Failure -> this
    }

    companion object {
        fun <T> success(value: T): AdbHubResult<T> = Success(value)
        fun failure(error: AdbHubError): AdbHubResult<Nothing> = Failure(error)
    }
}
