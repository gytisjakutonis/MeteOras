package gj.meteoras.ext.coroutines

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

data class Forced<T>(val value: T, val forced: Boolean = false)

typealias ForcedStateFlow<T> = MutableStateFlow<Forced<T>>

@OptIn(FlowPreview::class, ExperimentalTime::class)
fun <T> ForcedStateFlow<T>.distinct(timeout: Duration): Flow<T> =
    debounce { item ->
        if (item.forced) Duration.ZERO else timeout
    }.distinctUntilChanged { old, new ->
        if (new.forced) false else old == new
    }.map { item ->
        item.value
    }

fun <T> ForcedStateFlow(value: T): ForcedStateFlow<T> = MutableStateFlow(Forced(value))
