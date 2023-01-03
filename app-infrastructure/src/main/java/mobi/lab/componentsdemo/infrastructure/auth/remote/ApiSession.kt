package mobi.lab.componentsdemo.infrastructure.auth.remote

import androidx.annotation.Keep
import se.ansman.kotshi.JsonSerializable

@Keep
@JsonSerializable
data class ApiSession(val token: String?)
