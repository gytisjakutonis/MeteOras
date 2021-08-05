package gj.meteoras.repo

import java.util.concurrent.CancellationException

sealed class RepoResult<out T> {

    data class Error(val error: Throwable) : RepoResult<Nothing>()
    data class Success<out T>(val data: T) : RepoResult<T>()
    object Busy : RepoResult<Nothing>()
}

val <T> RepoResult<T>.data: T? get() = (this as? RepoResult.Success)?.data
val <T> RepoResult<T>.busy: Boolean get() = this is RepoResult.Busy
val <T> RepoResult<T>.error: Throwable? get() = (this as? RepoResult.Error)?.error

inline fun <T> tryResult(block: () -> T): RepoResult<T> = try {
    RepoResult.Success(block())
} catch (cancel: CancellationException) {
    throw cancel
} catch (error: Throwable) {
    RepoResult.Error(error)
}
