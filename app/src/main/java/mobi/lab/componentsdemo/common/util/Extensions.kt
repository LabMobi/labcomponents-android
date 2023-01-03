package mobi.lab.componentsdemo.common.util

import android.text.SpannableString
import android.text.Spanned
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import mobi.lab.componentsdemo.app.common.isStringEmpty
import mobi.lab.componentsdemo.domain.entities.DomainException
import mobi.lab.componentsdemo.domain.entities.ErrorCode
import timber.log.Timber

// TODO add more here
fun EditText.setTextAndSelection(text: CharSequence) {
    if (isStringEmpty(text)) {
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

fun Throwable.errorCode(): ErrorCode {
    return if (this is DomainException) code else ErrorCode.UNKNOWN
}
