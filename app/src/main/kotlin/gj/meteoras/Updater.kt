package gj.meteoras

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE
import com.google.android.play.core.install.model.InstallStatus.CANCELED
import com.google.android.play.core.install.model.InstallStatus.DOWNLOADED
import com.google.android.play.core.install.model.UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
import com.google.android.play.core.install.model.UpdateAvailability.UPDATE_AVAILABLE
import timber.log.Timber
import java.time.Duration
import java.time.Instant
import java.util.concurrent.Executors

class Updater(private val updateManager: AppUpdateManager) {

    private var cancelTimestamp: Instant = Instant.ofEpochMilli(0L)

    fun check(activity: Activity) {
        if (Duration.between(cancelTimestamp, Instant.now()) < AppConfig.updateTimeout) return

        updateManager.appUpdateInfo
            .addOnSuccessListener(Executors.newSingleThreadExecutor()) { info ->
                if (info.updateAvailability() == DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    info.start(activity)
                } else if (info.updateAvailability() == UPDATE_AVAILABLE && info.isUpdateTypeAllowed(FLEXIBLE)) {
                    info.start(activity)
                }
            }
            .addOnFailureListener { error ->
                Timber.e(error)
            }
    }

    private fun AppUpdateInfo.start(activity: Activity) {
        updateManager.startUpdateFlow(this, activity, AppUpdateOptions.newBuilder(FLEXIBLE).build())
            .addOnSuccessListener(Executors.newSingleThreadExecutor()) { result ->
                if (result == RESULT_OK) {
                    download()
                } else if (result == RESULT_CANCELED) {
                    cancelTimestamp = Instant.now()
                }
            }
            .addOnFailureListener { error ->
                Timber.e(error)
            }
    }

    private fun download() {
        lateinit var listener: (InstallState) -> Unit

        listener = { state: InstallState ->
            if (state.installStatus() == DOWNLOADED) {
                finish()
            } else if (state.installStatus() == CANCELED) {
                cancelTimestamp = Instant.now()
            }

            updateManager.unregisterListener(listener)
        }

        updateManager.registerListener(listener)
    }

    private fun finish() {
        updateManager.completeUpdate()
            .addOnSuccessListener(Executors.newSingleThreadExecutor()) {
                cancelTimestamp = Instant.ofEpochMilli(0L)
            }
            .addOnFailureListener { error ->
                Timber.e(error)
            }
    }
}
