package mobi.lab.componentsdemo.domain.entities

class DomainException(
    val code: ErrorCode,
    cause: Throwable? = null,
    message: String? = "DomainException: errorCode=$code",
) : Exception(message) {

    constructor(code: ErrorCode, cause: Throwable) : this(code = code, cause = cause, message = null)
    constructor(code: ErrorCode, message: String) : this(code = code, cause = null, message = message)

    init {
        if (cause != null) {
            this.initCause(cause)
        }
    }

    fun isFor(errorCode: ErrorCode): Boolean {
        return errorCode == code
    }

    companion object {
        fun unknown(): DomainException {
            return DomainException(ErrorCode.UNKNOWN)
        }

        fun unauthorized(cause: Throwable? = null): DomainException {
            return DomainException(ErrorCode.LOCAL_UNAUTHORIZED, cause)
        }
    }
}
