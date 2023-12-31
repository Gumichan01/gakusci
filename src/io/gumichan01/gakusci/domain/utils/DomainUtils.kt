package io.gumichan01.gakusci.domain.utils

import io.gumichan01.gakusci.domain.model.SearchResponse
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


fun SearchResponse.take(n: Int): SearchResponse {
    return this.copy(entries = entries.take(n))
}

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

fun SearchResponse.paginateForRest(n: Int, start: Int, numPerPage: Int?): SearchResponse {

    return entries.take(n).run {
        val range: IntRange = if (numPerPage == null || start + numPerPage - 1 > this.size)
            IntRange(start, this.size - 1) else IntRange(start, (start + numPerPage) - 1)

        this.slice(range).run { SearchResponse(this.size, start, this) }
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

fun defaultThumbnailLink(): String = "/image/not-found.jpg"