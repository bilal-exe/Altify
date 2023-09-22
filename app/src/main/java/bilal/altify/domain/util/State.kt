package bilal.altify.domain.util

sealed class State<T> {
    data class Success<T>(val t: T): State<T>()
    class Loading<T>: State<T>()
    data class Error<T>(val error: String): State<T>()
}

