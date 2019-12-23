package io.gumichan01.gakusci.controller.utils

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.utils.SearchType
import io.ktor.http.Parameters

fun retrieveWebParam(queryParameters: Parameters): Pair<QueryParam?, String> {
    return queryParameters["q"]?.let { query ->
        val rows = 1000
        val start = queryParameters["start"]?.toInt() ?: 0
        val searchType: SearchType? = getSearchTypeFrom(queryParameters)

        when {
            query.isBlank() -> Pair(null, "Bad request: query parameter 'q' is blank")
            start > rows -> Pair(null, "Bad request: start is greater than max_results")
            searchType == null -> Pair(null, "Bad request: no query parameter 'searchtype' provided")
            else -> Pair(QueryParam(query, searchType, rows, start), "")
        }
    } ?: Pair(null, "Bad request: no query parameter 'q' provided")
}

fun retrieveApiParam(queryParameters: Parameters, pathParameters: Parameters): Pair<QueryParam?, String> {
    return queryParameters["q"]?.let { query ->
        val start = queryParameters["start"]?.toInt() ?: 0
        val rows = queryParameters["max_results"]?.toInt() ?: 10
        val numPerPage: Int? = queryParameters["num_per_page"]?.toInt()
        val searchType: SearchType? = getApiSearchTypeFrom(pathParameters)

        when {
            query.isBlank() -> Pair(null, "Bad request: query parameter 'q' is blank")
            start > rows -> Pair(null, "Bad request: start is greater than max_results")
            searchType == null -> Pair(null, "Bad request: no query parameter 'searchtype' provided")
            numPerPage != null && numPerPage > rows -> {
                Pair(null, "Bad request: cannot get more entries per page than max_results")
            }
            else -> Pair(QueryParam(query, SearchType.RESEARCH, rows, start, numPerPage), "")
        }
    } ?: Pair(null, "Bad request: no query parameter 'q' provided")
}

private fun getSearchTypeFrom(parameters: Parameters): SearchType? {
    return parameters["searchtype"]?.let {
        when (it) {
            SearchType.RESEARCH.value -> SearchType.RESEARCH
            SearchType.BOOKS.value -> SearchType.BOOKS
            else -> null
        }
    }
}

private fun getApiSearchTypeFrom(parameters: Parameters): SearchType? {
    return parameters["search_type"]?.let {
        when (it) {
            SearchType.RESEARCHES.value -> SearchType.RESEARCH
            SearchType.BOOKS.value -> SearchType.BOOKS
            else -> null
        }
    }
}

