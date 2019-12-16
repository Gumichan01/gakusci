package io.gumichan01.gakusci.domain.model

data class QueryParam(val query: String, val start: Int = 0, val rows: Int = 10)