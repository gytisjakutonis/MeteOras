package gj.meteoras.ext.widget

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

typealias DrawableOnTouchListener = (
    view: View,
    event: MotionEvent,
    location: DrawableLocation
) -> Boolean

enum class DrawableLocation { Left, Top, Right, Bottom }

@SuppressLint("ClickableViewAccessibility")
fun TextView.setDrawableOnTouchListener(listener: DrawableOnTouchListener) {
    setOnTouchListener { view, event ->
        val (leftIcon, topIcon, rightIcon, bottomIcon) = compoundDrawables

        if (event.action == MotionEvent.ACTION_DOWN) {
            when {
                topIcon != null && event.rawY <= topIcon.bounds.height() ->
                    DrawableLocation.Top
                bottomIcon != null && event.rawY >= bottom - bottomIcon.bounds.height() ->
                    DrawableLocation.Bottom
                leftIcon != null && event.rawX <= leftIcon.bounds.width() ->
                    DrawableLocation.Left
                rightIcon != null && event.rawX >= right - rightIcon.bounds.width() ->
                    DrawableLocation.Right
                else -> null
            }?.let { location ->
                listener(view, event, location)
            } ?: false
        } else {
            false
        }
    }
}
