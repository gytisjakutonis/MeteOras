package gj.meteoras.ext.content

import android.content.SharedPreferences

fun SharedPreferences.getStrings(key: String): List<String> =
    getString(key, null)?.split(separator) ?: emptyList()

fun SharedPreferences.Editor.putStrings(key: String, value: List<String>) {
    putString(key, value.joinToString(separator))
}

private const val separator: String = 31.toChar().toString()
