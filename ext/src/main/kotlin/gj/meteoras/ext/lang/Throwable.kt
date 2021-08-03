package gj.meteoras.ext.lang

import timber.log.Timber

fun <T : Throwable> T.timber() {
    Timber.e(this)
}
