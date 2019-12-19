package io.gumichan01.gakusci.controller.utils

import io.gumichan01.gakusci.domain.model.QueryParam
import io.ktor.http.Parameters

fun retrieveWebParam(queryParameters: Parameters): Pair<QueryParam?, String> {
    return queryParameters["q"]?.let { query ->
        val rows = 1000
        val start = queryParameters["start"]?.toInt() ?: 0

        if (start > rows) {
            Pair(null, "Bad request: start is greater than max_results")
        } else {
            Pair(QueryParam(query, rows, start), "")
        }
    } ?: Pair(null, "Bad request: no query parameter 'q' provided")
}


fun retrieveApiParam(queryParameters: Parameters): Pair<QueryParam?, String> {
    return queryParameters["q"]?.let { query ->
        val start = queryParameters["start"]?.toInt() ?: 0
        val rows = queryParameters["max_results"]?.toInt() ?: 10
        val numPerPage: Int? = queryParameters["num_per_page"]?.toInt()

        when {
            start > rows -> Pair(null, "Bad request: start is greater than max_results")
            numPerPage != null && numPerPage > rows -> {
                Pair(null, "Bad request: cannot get more entries per page than max_results")
            }
            else -> Pair(QueryParam(query, rows, start, numPerPage), "")
        }
    } ?: Pair(null, "Bad request: no query parameter 'q' provided")
}