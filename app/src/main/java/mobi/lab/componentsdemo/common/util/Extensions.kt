package mobi.lab.componentsdemo.common.util

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

fun EditText.setTextAndSelection(text: CharSequence) {
    if (TextUtils.isEmpty(text)) {
        getText().clear()
    } else {
        setText(text)
        setSelection(text.length)
    }
}

/**
 * Since isVisible can't be overridden, then we need to implement our own
 * If a nested fragment's parent fragment is hidden, then nested fragment's visibility does not change.
 * To fix that, we need to look at all the parent fragments and check their visibility.
 */
fun Fragment.isFragmentVisible(): Boolean {
    Timber.d("isFragmentVisible fragment=$this visible=$isVisible")
    val parent = parentFragment
    if (parent != null) {
        return isVisible && parent.isFragmentVisible()
    }
    return isVisible
}

fun SpannableString.addSpan(spannablePart: String, span: Any): SpannableString {
    val start = this.indexOf(spannablePart)
    val end = start + spannablePart.length
    if (start > -1) {
        setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
    }
    return this
}

fun RecyclerView.smoothScrollToTop(smoothOffsetY: Int = 10) {
    val manager = layoutManager
    if (manager == null) {
        Timber.w("smoothScrollToTop layoutManager is null")
        return
    }

    if (manager is LinearLayoutManager) {
        val offsetY = manager.findLastVisibleItemPosition()
        if (offsetY > smoothOffsetY) {
            manager.scrollToPosition(smoothOffsetY)
        }
    }
    val scroller = object : LinearSmoothScroller(context) {
        override fun getVerticalSnapPreference(): Int {
            return SNAP_TO_START
        }
    }
    scroller.targetPosition = 0
    manager.startSmoothScroll(scroller)
}

/**
 * when statement only creates a compiler error for sealed classes when used as an expression.
 * Use this .exhaustive extensions in other cases to get the compiler error when not all available options are defined.
 *
 * sealed class A
 *
 * class B : A()
 * class C : A()
 *
 * // Compiler error
 * val y = when(x) {
 *   is B ->
 * }
 *
 * // No compiler error
 * when(x) {
 *   is B ->
 * }
 *
 * // Compiler error
 * when(x) {
 *   is B ->
 * }.exhaustive
 */
val <T> T.exhaustive: T
    get() = this
