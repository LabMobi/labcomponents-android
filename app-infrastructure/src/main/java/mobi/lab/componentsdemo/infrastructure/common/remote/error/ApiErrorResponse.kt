package mobi.lab.componentsdemo.infrastructure.common.remote.error

import androidx.annotation.Keep
import se.ansman.kotshi.JsonSerializable
import java.time.Instant

@Keep
@JsonSerializable
data class ApiErrorResponse(
    val httpStatus: Int,
    val httpError: String,
    val instant: Instant?,
    val code: String?,
    val message: String?,
)
