package mobi.lab.componentsdemo.app.common

import java.time.OffsetDateTime

fun OffsetDateTime.toEpochMilli() = this.toInstant().toEpochMilli()
