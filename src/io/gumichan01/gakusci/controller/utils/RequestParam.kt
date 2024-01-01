package io.gumichan01.gakusci.controller.utils

import io.gumichan01.gakusci.domain.utils.SearchType

data class RequestParam(
    val query: String,
    val searchType: SearchType,
    val rows: Int,
    val start: Int
) : IRequestParamResult