package gj.meteoras.ext.binding

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

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

@BindingAdapter("listItems")
fun RecyclerView.setListItems(items: List<Any>?) {
    (adapter as? ListAdapter<Any, *>)?.submitList(items) {
        scrollToPosition(0)
    }
}

@BindingAdapter("icons")
fun TextView.setIcons(icons: Array<Int?>?) {
    val drawables = icons?.map { icon ->
        icon?.let { ContextCompat.getDrawable(context, it) }
    } ?: listOf(null, null, null, null)

    setCompoundDrawablesWithIntrinsicBounds(
        drawables[0],
        drawables[1],
        drawables[2],
        drawables[3],
    )
}
