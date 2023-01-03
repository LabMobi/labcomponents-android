package mobi.lab.componentsdemo.infrastructure.common.http

import mobi.lab.componentsdemo.infrastructure.common.json.Json
import mobi.lab.componentsdemo.infrastructure.common.json.fromJson
import mobi.lab.componentsdemo.infrastructure.common.platform.NetworkMonitor
import mobi.lab.componentsdemo.infrastructure.common.remote.error.ApiErrorResponse
import mobi.lab.componentsdemo.infrastructure.common.remote.error.ApiErrorResponseMapper
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

internal class MyErrorTransformer(
    private val networkMonitor: NetworkMonitor,
    private val errorMapper: ApiErrorResponseMapper,
    private val json: Json
) : ErrorTransformer {
    override fun transform(error: Throwable): Throwable {
        if (error is HttpException) {
            return transformHttpException(error)
        } else if (error is UnknownHostException || !networkMonitor.isConnected()) {
            return errorMapper.toNetworkEntity(error)
        }
        return error
    }

    @Suppress("ReturnCount")
    private fun transformHttpException(error: HttpException): Throwable {
        if (error.code() == HTTP_CODE_403_UNAUTHORIZED) {
            return errorMapper.toUnauthorizedEntity(error)
        }

        var rawError: String? = null
        try {
            val errorBody = error.response()?.errorBody() ?: return error
            rawError = errorBody.string()
            val parsedError = json.fromJson<ApiErrorResponse>(rawError) ?: return error
            return errorMapper.toEntity(error, parsedError)
        } catch (error: Exception) {
            // Error parsing exception, pass through the original
            Timber.w(error, "transformException, raw=$rawError")
            return error
        }
    }

    companion object {
        private const val HTTP_CODE_403_UNAUTHORIZED = 403
    }
}
