package gj.meteoras.ext.lang

import kotlin.coroutines.cancellation.CancellationException

inline fun <R> result(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (cancel: CancellationException) {
        throw cancel
    } catch (error: Throwable) {
        Result.failure(error)
    }
}
