package mobi.lab.componentsdemo.infrastructure.common.remote.error

import mobi.lab.componentsdemo.domain.entities.DomainException
import mobi.lab.componentsdemo.domain.entities.ErrorCode
import retrofit2.HttpException

internal class ApiErrorResponseMapper {

    fun toEntity(error: HttpException, item: ApiErrorResponse): DomainException {
        val domainException = DomainException(ErrorCode.parse(item.code))
        domainException.initCause(error.cause)
        return domainException
    }

    fun toUnauthorizedEntity(cause: Throwable): DomainException {
        val domainException = DomainException(ErrorCode.LOCAL_UNAUTHORIZED)
        domainException.initCause(cause)
        return domainException
    }

    fun toNetworkEntity(cause: Throwable): DomainException {
        val domainException = DomainException(ErrorCode.LOCAL_NO_NETWORK)
        domainException.initCause(cause)
        return domainException
    }
}
