package gj.meteoras.ui

import android.content.SharedPreferences
import androidx.core.content.edit
import gj.meteoras.ext.content.getStrings
import gj.meteoras.ext.content.putStrings

class UiPreferences(private val sharedPreferences: SharedPreferences) {

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

private const val favouritePlacesKey = "favouritePlaces"
private const val disclaimerAcceptedKey = "disclaimerAccepted"
