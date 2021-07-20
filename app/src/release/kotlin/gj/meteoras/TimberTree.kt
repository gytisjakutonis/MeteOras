package gj.meteoras

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class TimberTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority > Log.DEBUG) {
            t?.let {
                FirebaseCrashlytics.getInstance().recordException(it)
            } ?: FirebaseCrashlytics.getInstance().log(message)
        }
    }
}
