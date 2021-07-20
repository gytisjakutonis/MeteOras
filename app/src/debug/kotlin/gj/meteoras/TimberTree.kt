package gj.meteoras

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class TimberTree : Timber.DebugTree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)

        t?.let {
            FirebaseCrashlytics.getInstance().recordException(it)
        } ?: FirebaseCrashlytics.getInstance().log(message)
    }
}
