package gj.meteoras

import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class TimberTree : Timber.DebugTree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)

        FirebaseCrashlytics.getInstance().log(message)

        t?.let {
            FirebaseCrashlytics.getInstance().recordException(it)
        }
    }
}
