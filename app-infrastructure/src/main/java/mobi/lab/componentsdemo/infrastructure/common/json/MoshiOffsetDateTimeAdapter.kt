package mobi.lab.componentsdemo.infrastructure.common.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 * A Moshi adapter capable of parsing dates as OffsetDateTime instances.
 *
 * Supported example formats include:
 * 2011-12-03T10:15:30.000Z
 * 2011-12-03T10:15:30.000+02:00
 *
 * Milliseconds are optional.
 */
class MoshiOffsetDateTimeAdapter : JsonAdapter<OffsetDateTime>() {

    private val formatter = MoshiTemporalAccessorFormatter(
        outputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME,
        inputFormatters = listOf(DateTimeFormatter.ISO_OFFSET_DATE_TIME, DateTimeFormatter.ISO_INSTANT)
    )

    override fun toJson(writer: JsonWriter, value: OffsetDateTime?) {
        if (value == null) {
            writer.nullValue()
        } else {
            val stringValue = formatter.format(value)
            writer.value(stringValue)
        }
    }

    override fun fromJson(reader: JsonReader): OffsetDateTime? {
        return if (reader.peek() == JsonReader.Token.NULL) {
            reader.nextNull()
        } else {
            OffsetDateTime.from(formatter.parse(reader.nextString()))
        }
    }
}
