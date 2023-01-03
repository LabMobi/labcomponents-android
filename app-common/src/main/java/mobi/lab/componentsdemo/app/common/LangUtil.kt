package mobi.lab.componentsdemo.app.common

fun isStringEmpty(string: CharSequence?): Boolean {
    return string.isNullOrEmpty()
}

fun stringEquals(a: CharSequence?, b: CharSequence?): Boolean {
    // Taken from TextUtils.equals
    if (a === b) return true
    val length = a?.length ?: 0
    if (a != null && b != null && length == b.length) {
        if (a is String && b is String) {
            return a == b
        } else {
            return charSequenceEqualsCharByChar(length, a, b)
        }
    }
    return false
}

private fun charSequenceEqualsCharByChar(compareLength: Int, a: CharSequence, b: CharSequence): Boolean {
    for (i in 0 until compareLength) {
        if (a[i] != b[i]) return false
    }
    return true
}

fun Any.className(): String {
    return this::class.java.name
}

fun isListEmpty(list: List<*>?): Boolean {
    return list == null || list.isEmpty()
}

fun Int.hasFlag(flag: Int) = flag and this == flag

fun Int.setFlag(flag: Int) = this or flag

fun Int.unsetFlag(flag: Int) = this and flag.inv()

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
