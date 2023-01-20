package gj.meteoras.ext

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

fun <R, T> alias(delegate: KMutableProperty0<T>) = object : ReadWriteProperty<R, T> {

    override fun getValue(thisRef: R, property: KProperty<*>): T = delegate.get()

    override fun setValue(thisRef: R, property: KProperty<*>, value: T) {
        delegate.set(value)
    }
}
