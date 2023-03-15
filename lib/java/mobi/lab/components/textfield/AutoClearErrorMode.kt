package mobi.lab.components.textfield

/**
 * [LabTextField] automatic error clearing modes.
 */
public enum class AutoClearErrorMode {

    /**
     * Error state is not cleared automatically.
     */
    DISABLED,

    /**
     * Error state is cleared when text changed.
     */
    ON_TEXT_CHANGED,

    /**
     * Error state is cleared when the TextField is tapped.
     */
    ON_TOUCH;

    internal fun value(): Int {
        return when (this) {
            DISABLED -> VALUE_DISABLED
            ON_TEXT_CHANGED -> VALUE_ON_TEXT_CHANGED
            ON_TOUCH -> VALUE_ON_TOUCH
        }
    }

    internal companion object {
        private const val VALUE_ON_TOUCH = 2
        private const val VALUE_ON_TEXT_CHANGED = 1
        private const val VALUE_DISABLED = 0

        fun parse(value: Int): AutoClearErrorMode {
            return when (value) {
                VALUE_ON_TOUCH -> ON_TOUCH
                VALUE_ON_TEXT_CHANGED -> ON_TEXT_CHANGED
                else -> DISABLED
            }
        }
    }
}
