package gj.meteoras

import android.os.Bundle
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import timber.log.Timber

object Monitor {

    fun debug(message: String) {
        Timber.d(message)
    }

    fun info(message: String) {
        Timber.i(message)
    }

    fun warning(message: String) {
        Timber.w(message)
    }

    fun error(message: String) {
        Timber.e(message)
    }

    fun error(error: Throwable) {
        Timber.e(error)
    }

    fun event(name: String, data: Bundle? = null) {
        Firebase.analytics.logEvent(name, data)
    }
}
