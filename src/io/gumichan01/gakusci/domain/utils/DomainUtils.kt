package io.gumichan01.gakusci.domain.utils

import io.gumichan01.gakusci.domain.model.SearchResponse

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
