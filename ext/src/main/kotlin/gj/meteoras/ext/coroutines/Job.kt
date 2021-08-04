package gj.meteoras.ext.coroutines

import kotlinx.coroutines.Job

fun Job.then(block: (Throwable?) -> Unit): Job {
    invokeOnCompletion(block)
    return this
}
