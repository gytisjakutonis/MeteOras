package gj.meteoras.ext.lang

import timber.log.Timber

inline fun <T> tryOrNull(block: () -> T): T? = try {
    block()
} catch (t: Throwable) {
    Timber.e(t)
    null
}
