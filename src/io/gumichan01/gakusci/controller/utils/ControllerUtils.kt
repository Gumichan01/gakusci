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
            searchType == null -> Pair(null, "Bad request: no searchtype provided")
            else -> Pair(QueryParam(query, searchType, rows, start), "")
        }
    } ?: Pair(null, "Bad request: no query parameter 'q' provided")
}

// NOTE For the sake of simplicity, research is the default search type
// TODO handle queries properly the path paramater determines the search type
fun retrieveApiParam(queryParameters: Parameters): Pair<QueryParam?, String> {
    return queryParameters["q"]?.let { query ->
        val start = queryParameters["start"]?.toInt() ?: 0
        val rows = queryParameters["max_results"]?.toInt() ?: 10
        val numPerPage: Int? = queryParameters["num_per_page"]?.toInt()

        when {
            query.isBlank() -> {
                Pair(null, "Bad request: query parameter 'q' is blank")
            }
            start > rows -> {
                Pair(null, "Bad request: start is greater than max_results")
            }
            numPerPage != null && numPerPage > rows -> {
                Pair(null, "Bad request: cannot get more entries per page than max_results")
            }
            else -> Pair(QueryParam(query, SearchType.RESEARCH, rows, start, numPerPage), "")
        }
    } ?: Pair(null, "Bad request: no query parameter 'q' provided")
}

@Deprecated("Don't use this", ReplaceWith("getSearchType"))
fun retrieveSearchType(queryParameters: Parameters): String? = queryParameters["searchtype"]

fun getSearchTypeFrom(queryParameters: Parameters): SearchType? {
    return queryParameters["searchtype"]?.let {
        when (it) {
            SearchType.RESEARCH.value -> SearchType.RESEARCH
            SearchType.BOOKS.value -> SearchType.BOOKS
            else -> null
        }
    }
}

