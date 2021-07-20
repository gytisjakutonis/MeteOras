package gj.meteoras

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        FirebaseCrashlytics.getInstance().sendUnsentReports()

        Timber.plant(TimberTree())
    }

}
