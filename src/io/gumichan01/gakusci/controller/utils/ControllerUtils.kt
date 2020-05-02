package io.gumichan01.gakusci.controller.utils

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.SearchResponse
import io.gumichan01.gakusci.domain.utils.SearchType
import io.ktor.http.Parameters

private val MAX_ENTRIES = 2000

fun retrieveWebParam(queryParameters: Parameters): Pair<QueryParam?, String> {
    return queryParameters["q"]?.let { query ->
        val defaultRows = 100
        val numPerPage = 10
        val start = queryParameters["start"]?.toInt() ?: 0
        val searchType: SearchType? = getSearchTypeFrom(queryParameters)
        // The webapp must limitate the number of maximum entries in the
        val rows = if (start + numPerPage > defaultRows) {
            if (start * 2 > MAX_ENTRIES) MAX_ENTRIES else start * 2
        } else defaultRows

        when {
            query.isBlank() -> Pair(null, "Bad request: query parameter 'q' is blank")
            start < 0 -> Pair(null, "Bad request: negative start value: $start")
            start > rows -> Pair(null, "Bad request: start is greater than max_results")
            searchType == null -> Pair(null, "Bad request: no query parameter 'stype' provided")
            else -> Pair(QueryParam(query, searchType, rows, start, numPerPage), "")
        }
    } ?: Pair(null, "Bad request: no query parameter 'q' provided")
}

fun retrieveApiParam(queryParameters: Parameters, pathParameters: Parameters): Pair<QueryParam?, String> {
    return queryParameters["q"]?.let { query ->
        getApiSearchTypeFrom(pathParameters)?.let { searchType ->
            val start: Int? = queryParameters["start"]?.toInt()
            val rows: Int? = queryParameters["max_results"]?.toInt()
            val numPerPage: Int? = queryParameters["num_per_page"]?.toInt()

            when {
                rows != null && rows < 0 -> Pair(null, "Bad request: negative max_results value: $rows")
                start != null -> {
                    RestApiQueryParameters(query, searchType, rows, start, numPerPage).run {
                        retrieveAndCheckRestApiQueryParameters(this)
                    }
                }
                numPerPage != null -> Pair(null, "Bad request: cannot set 'num_per_page' with no 'start' value")
                else -> {
                    val maxResults = rows ?: 10
                    Pair(QueryParam(query, searchType, maxResults, 0, numPerPage), "")
                }
            }
        } ?: Pair(null, "Bad request: no query parameter 'stype' provided")
    } ?: Pair(null, "Bad request: no query parameter 'q' provided")
}

private fun retrieveAndCheckRestApiQueryParameters(queryParameters: RestApiQueryParameters): Pair<QueryParam?, String> {
    return queryParameters.run {
        when {
            start < 0 -> Pair(null, "Bad request: negative 'start' value: $start")
            numPerPage == null -> Pair(null, "Bad request: cannot set 'start' with no 'num_per_page'")
            numPerPage < 0 -> Pair(null, "Bad request: negative 'num_per_page' value: $start")
            rows == null -> Pair(null, "Bad request: cannot set 'start' with no 'max_results'")
            start > rows -> Pair(null, "Bad request: 'start' is greater than 'max_results'")
            numPerPage > rows -> Pair(null, "Bad request: 'num_per_page' is greater than 'max_results'")
            else -> Pair(QueryParam(query, searchType, rows, start, numPerPage), "")
        }
    }
}

private data class RestApiQueryParameters(
    val query: String,
    val searchType: SearchType,
    val rows: Int?,
    val start: Int,
    val numPerPage: Int?
)

private fun getSearchTypeFrom(parameters: Parameters): SearchType? {
    return parameters["stype"]?.let {
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

fun SearchResponse.isEmpty(): Boolean {
    return totalResults == 0 && entries.isEmpty()
}