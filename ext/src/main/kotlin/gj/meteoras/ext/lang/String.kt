package gj.meteoras.ext.lang

import android.icu.text.Normalizer2
import java.util.regex.Pattern

fun String.normalise(): String = normaliser.normalize(this).replace(InCombiningDiacriticalMarks, "")

private val normaliser = Normalizer2.getNFKDInstance()
private val InCombiningDiacriticalMarks = Pattern.compile("\\p{InCombiningDiacriticalMarks}+").toRegex()
