package io.gumichan01.gakusci.domain.model

import io.gumichan01.gakusci.domain.model.entry.SimpleResultEntry

data class SearchResponse(val totalResults: Int, val start: Int, val entries: List<SimpleResultEntry>)