package gj.meteoras.repo

import android.content.SharedPreferences
import androidx.core.content.edit
import gj.meteoras.ext.content.getStrings
import gj.meteoras.ext.content.putStrings
import java.time.Instant

class RepoPreferences(private val sharedPreferences: SharedPreferences) {

    var placesTimestamp: Instant
        get() = Instant.ofEpochSecond(sharedPreferences.getLong(placesTimestampKey, 0L))
        set(value) {
            sharedPreferences.edit {
                putLong(placesTimestampKey, value.epochSecond)
            }
        }

    var favouritePlaces: List<String>
        get() = sharedPreferences.getStrings(favouritePlacesKey)
        set(value) {
            sharedPreferences.edit {
                putStrings(favouritePlacesKey, value)
            }
        }

    var disclaimerAccepted: Boolean
        get() = sharedPreferences.getBoolean(disclaimerAcceptedKey, false)
        set(value) {
            sharedPreferences.edit {
                putBoolean(disclaimerAcceptedKey, value)
            }
        }
}

private const val placesTimestampKey = "placesTimestamp"
private const val favouritePlacesKey = "favouritePlaces"
private const val disclaimerAcceptedKey = "disclaimerAccepted"
