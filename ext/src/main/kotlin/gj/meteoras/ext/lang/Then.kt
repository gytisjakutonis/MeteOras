package gj.meteoras.ext.lang

inline fun Boolean.then(block: () -> Unit): Boolean {
    if (this) {
        block()
    }

    return this
}
