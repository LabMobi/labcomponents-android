package mobi.lab.componentsdemo.domain.entities

enum class ErrorCode(val code: String) {
    // Local errors
    UNKNOWN("local-unknown"),
    LOCAL_UNAUTHORIZED("local-unauthorized"),
    LOCAL_NO_NETWORK("local-no-network"),
    LOCAL_INVALID_CREDENTIALS("local-invalid-credentials");

    companion object {
        fun parse(code: String?): ErrorCode {
            if (code == null) {
                return UNKNOWN
            }
            for (domainCode in values()) {
                if (domainCode.code == code) {
                    return domainCode
                }
            }
            return UNKNOWN
        }
    }
}
