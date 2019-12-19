package io.gumichan01.gakusci.domain.model

data class QueryParam(val query: String, val rows: Int = 10, val start: Int = 0, val numPerPage: Int? = null)