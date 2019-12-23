package io.gumichan01.gakusci.domain.model

import io.gumichan01.gakusci.domain.utils.SearchType

data class QueryParam(
    val query: String,
    val searchType: SearchType,
    val rows: Int = 10,
    val start: Int = 0,
    val numPerPage: Int? = null
)