package mobi.lab.componentsdemo.infrastructure.common.json

import com.squareup.moshi.JsonDataException
import mobi.lab.componentsdemo.app.common.toEpochMilli
import org.junit.Assert
import org.junit.Test
import java.time.OffsetDateTime
import java.time.ZoneOffset
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class MoshiOffsetDateTimeAdapterTest {

    private val json = MoshiJson(MoshiFactory.get())

    @Test
    fun parse_from_valid_zulu() {
        val zuluString = "\"2021-03-24T12:00:00Z\""
        val time = parseOffsetDateTime(zuluString)

        assertNotNull(time)
        assertEquals(ZoneOffset.UTC, time.offset)
        assertEquals(BASE_TIME_MILLIS, time.toEpochMilli())

        val formattedString = json.toJson(time)
        assertEquals(zuluString, formattedString)
    }

    @Test
    fun parse_from_valid_zulu_with_milliseconds() {
        val zuluString = "\"2021-03-24T12:00:00.01Z\""
        val time = parseOffsetDateTime(zuluString)

        assertNotNull(time)
        assertEquals(ZoneOffset.UTC, time.offset)
        assertEquals(BASE_TIME_MILLIS + 10, time.toEpochMilli())

        val formattedString = json.toJson(time)
        assertEquals(zuluString, formattedString)
    }

    @Test
    fun parse_from_valid_zulu_with_max_milliseconds() {
        val zuluString = "\"2021-03-24T12:00:00.010000000Z\""
        val time = parseOffsetDateTime(zuluString)

        assertNotNull(time)
        assertEquals(ZoneOffset.UTC, time.offset)
        assertEquals(BASE_TIME_MILLIS + 10, time.toEpochMilli())

        val formattedString = json.toJson(time)
        assertEquals("\"2021-03-24T12:00:00.01Z\"", formattedString)
    }

    @Test
    fun throw_error_when_parse_from_invalid_zulu_too_many_milliseconds() {
        val offsetString = "\"2021-03-24T12:00:00.0100000000Z\""
        Assert.assertThrows(JsonDataException::class.java) { parseOffsetDateTime(offsetString) }
    }

    @Test
    fun parse_from_valid_offset() {
        val offsetString = "\"2021-03-24T12:00:00+02:00\""
        val time = parseOffsetDateTime(offsetString)

        assertNotNull(time)
        assertEquals(ZoneOffset.ofHours(2), time.offset)
        assertEquals(BASE_TIME_MILLIS - 2 * HOUR_MILLIS, time.toEpochMilli())

        val formattedString = json.toJson(time)
        assertEquals(offsetString, formattedString)
    }

    @Test
    fun parse_from_valid_offset_with_milliseconds() {
        val offsetString = "\"2021-03-24T12:00:00.02+02:00\""
        val time = parseOffsetDateTime(offsetString)

        assertNotNull(time)
        assertEquals(ZoneOffset.ofHours(2), time.offset)
        assertEquals(BASE_TIME_MILLIS - 2 * HOUR_MILLIS + 20, time.toEpochMilli())

        val formattedString = json.toJson(time)
        assertEquals(offsetString, formattedString)
    }

    @Test
    fun parse_from_valid_offset_with_max_milliseconds() {
        val offsetString = "\"2021-03-24T12:00:00.020000000+02:00\""
        val time = parseOffsetDateTime(offsetString)

        assertNotNull(time)
        assertEquals(ZoneOffset.ofHours(2), time.offset)
        assertEquals(BASE_TIME_MILLIS - 2 * HOUR_MILLIS + 20, time.toEpochMilli())

        val formattedString = json.toJson(time)
        assertEquals("\"2021-03-24T12:00:00.02+02:00\"", formattedString)
    }

    @Test
    fun throw_error_when_parse_from_invalid_offset_too_many_milliseconds() {
        val offsetString = "\"2021-03-24T12:00:00.0100000000+02:00\""
        Assert.assertThrows(JsonDataException::class.java) { parseOffsetDateTime(offsetString) }
    }

    @Test
    fun throw_error_when_parse_from_invalid_format() {
        val zuluString = "\"2021-03-24T12:00:00\""
        Assert.assertThrows(JsonDataException::class.java) { parseOffsetDateTime(zuluString) }
    }

    @Test
    fun parse_from_null() {
        val zuluString = "null"
        val time = parseOffsetDateTime(zuluString)

        assertNull(time)

        val formattedString = json.toJson<OffsetDateTime>(null)
        assertEquals(zuluString, formattedString)
    }

    @Test
    fun throw_error_when_parse_from_empty_string() {
        Assert.assertThrows(JsonDataException::class.java) { parseOffsetDateTime("\"\"") }
        Assert.assertThrows(JsonDataException::class.java) { parseOffsetDateTime("{}") }
    }

    private fun parseOffsetDateTime(input: String): OffsetDateTime? {
        return json.fromJson(input)
    }

    companion object {
        private const val BASE_TIME_MILLIS = 1616587200000L
        private const val HOUR_MILLIS = 1000 * 60 * 60L
    }
}
