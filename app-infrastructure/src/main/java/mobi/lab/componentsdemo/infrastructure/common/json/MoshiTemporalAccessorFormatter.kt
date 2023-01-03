package mobi.lab.componentsdemo.infrastructure.common.json

import com.squareup.moshi.JsonDataException
import java.time.DateTimeException
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.TemporalAccessor

class MoshiTemporalAccessorFormatter(
    private val outputFormatter: DateTimeFormatter,
    private val inputFormatters: List<DateTimeFormatter>
) {

    @Throws(JsonDataException::class)
    fun parse(value: String): TemporalAccessor {
        for (formatter in inputFormatters) {
            try {
                return formatter.parse(value)
            } catch (error: DateTimeParseException) {
                continue
            }
        }
        throw JsonDataException("No matching formats found")
    }

    @Throws(JsonDataException::class)
    fun format(temporal: TemporalAccessor): String {
        try {
            return outputFormatter.format(temporal)
        } catch (error: DateTimeException) {
            throw JsonDataException(error)
        }
    }
}
