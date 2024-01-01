package io.gumichan01.gakusci.domain.utils

import io.gumichan01.gakusci.domain.model.SearchResponse
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


fun SearchResponse.isEmpty(): Boolean {
    return totalResults == 0 && entries.isEmpty()
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