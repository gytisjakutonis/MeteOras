package gj.meteoras.repo

import android.content.SharedPreferences
import androidx.core.content.edit
import java.time.Instant

class RepoPreferences(private val sharedPreferences: SharedPreferences) {

    var placesTimestamp: Instant
        get() = Instant.ofEpochSecond(sharedPreferences.getLong(placesTimestampKey, 0L))
        set(value) {
            sharedPreferences.edit {
                putLong(placesTimestampKey, value.epochSecond)
            }
        }
}

private const val placesTimestampKey = "placesTimestamp"
