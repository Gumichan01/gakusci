package io.gumichan01.gakusci.domain.utils

import io.gumichan01.gakusci.client.jikan.DateInterval
import io.gumichan01.gakusci.domain.model.SearchResponse
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

fun SearchResponse.take(n: Int) = this.copy(entries = entries.take(n))

fun SearchResponse.slice(start: Int, numPerPage: Int?): SearchResponse {
    return numPerPage?.let { slice(IntRange(start, start + numPerPage - 1)) } ?: this
}

fun SearchResponse.slice(range: IntRange): SearchResponse {
    return if (range.last > entries.size) {
        this.copy(entries = entries.slice(IntRange(range.first, entries.size - 1)))
    } else {
        this.copy(entries = entries.slice(range))
    }
}

fun DateInterval.toText(): String {
    val localStartDate: LocalDate = startDate.toLocalDate()
    val localEndDate: LocalDate? = endDate?.toLocalDate()
    return when {
        localEndDate == null -> "(${localStartDate.year} - ...)"
        localStartDate.year == localEndDate.year -> "(${localEndDate.year})"
        else -> "(${localStartDate.year} - ${localEndDate.year})"
    }
}

fun Date.toLocalDate(): LocalDate {
    return toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
}

fun String.toLocalDate(formatter: DateTimeFormatter): LocalDate {
    return LocalDate.parse(this, formatter)
}