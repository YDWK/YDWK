/*
 * Copyright 2023 YDWK inc.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.yde.util

import java.time.*
import java.time.chrono.ChronoZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.TemporalAccessor
import java.util.*

fun formatZonedDateTime(time: String): String {
    return if (time == "null") {
        "null"
    } else {
        val formatter =
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                .withLocale(Locale.ENGLISH)
                .withZone(ZoneId.systemDefault())

        formatter.format(ZonedDateTime.parse(time))
    }
}

fun reverseFormatZonedDateTime(time: String): ZonedDateTime {
    val formatter =
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
            .withLocale(Locale.ENGLISH)
            .withZone(ZoneId.systemDefault())

    return ZonedDateTime.from(formatter.parse(time))
}

fun convertInstantToChronoZonedDateTime(instant: Instant): ChronoZonedDateTime<*> {
    return instant.atZone(ZoneId.systemDefault())
}

fun toOffsetDateTime(time: TemporalAccessor): OffsetDateTime {
    if (time is OffsetDateTime) {
        return time
    } else {

        val offset: ZoneOffset =
            try {
                ZoneOffset.from(time)
            } catch (e: DateTimeException) {
                ZoneOffset.UTC
            }

        return try {
            val localDateTime = LocalDateTime.from(time)
            OffsetDateTime.of(localDateTime, offset)
        } catch (e: DateTimeException) {
            try {
                val instant = Instant.from(time)
                OffsetDateTime.ofInstant(instant, offset)
            } catch (e: DateTimeException) {
                throw DateTimeException(
                    "Unable to obtain OffsetDateTime from TemporalAccessor: $time of type ${time.javaClass.name}")
            }
        }
    }
}
