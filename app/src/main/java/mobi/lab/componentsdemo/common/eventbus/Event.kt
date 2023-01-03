package mobi.lab.componentsdemo.common.eventbus

open class Event {
    val error: Throwable?
    val requestCode: Int

    constructor(requestCode: Int = REQUEST_GENERAL) : this(null, requestCode)
    constructor(error: Throwable?, requestCode: Int = REQUEST_GENERAL) {
        this.error = error
        this.requestCode = requestCode
    }

    fun hasError(): Boolean {
        return error != null
    }

    companion object {
        const val REQUEST_GENERAL = -1
    }
}
