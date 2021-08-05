package gj.meteoras.ext.binding

import android.view.View
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun View.setVisible(visible: Boolean?) {
    isInvisible = visible != true
}

fun interface TextChangedListener {
    operator fun invoke(text: String?)
}

@BindingAdapter("onTextChanged")
fun TextView.setTextChangedListener(listener: TextChangedListener) {
    var lastValue: String? = null

    addTextChangedListener(
        onTextChanged = { value, _, _, _ ->
            val text = value?.toString()
            if (lastValue != text) {
                lastValue = text
                listener(text)
            }
        }
    )
}
