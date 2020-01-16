package io.gumichan01.gakusci.domain.model

import io.gumichan01.gakusci.domain.model.entry.IResultEntry

data class SearchResponse(val totalResults: Int, val start: Int, val entries: List<IResultEntry>)