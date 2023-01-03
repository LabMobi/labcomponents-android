package mobi.lab.componentsdemo.infrastructure.common.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import timber.log.Timber
import java.io.IOException
import java.lang.reflect.Type

class MoshiJson(private val moshi: Moshi) : Json {

    private fun <T> createAdapter(typeToken: Type): JsonAdapter<T> {
        val adapter: JsonAdapter<T> = moshi.adapter(typeToken)
        return adapter.nullSafe()
    }

    override fun <T : Any> toJson(item: T?, typeToken: Type): String {
        return createAdapter<T>(typeToken).toJson(item)
    }

    override fun <T : Any> fromJson(rawJson: String, typeToken: Type): T? {
        return try {
            return createAdapter<T>(typeToken).fromJson(rawJson)
        } catch (error: IOException) {
            Timber.e(error, "fromJson type=$typeToken raw=$rawJson")
            null
        }
    }
}
