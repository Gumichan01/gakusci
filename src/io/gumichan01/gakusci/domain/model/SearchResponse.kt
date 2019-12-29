package io.gumichan01.gakusci.domain.model

data class SearchResponse(val totalResults: Int, val start: Int, val entries: List<ResultEntry>) {
    fun isEmpty(): Boolean {
        return totalResults == 0 && entries.isEmpty()
    }
}