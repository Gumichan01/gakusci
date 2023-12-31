package io.gumichan01.gakusci.controller.utils

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.SearchResponse
import io.gumichan01.gakusci.domain.utils.SearchType
import io.ktor.http.*

const val MAX_ENTRIES = 1000
const val MINIMUM_QUERY_LENGTH = 3
const val BANG = '!'

fun retrieveWebParam(queryParameters: Parameters): IRequestParamResult {
    return queryParameters["q"]?.let { query ->
        val numPerPage = 10
        val start: Int = queryParameters["start"]?.toInt() ?: 0
        val searchType: SearchType? = getSearchTypeFrom(queryParameters)
        // The webapp must not retrieve more than 1000 entries, for the sake of performance
        val rows: Int = MAX_ENTRIES

        when {
            query.isBlank() -> BadRequest("Query parameter 'q' is blank")
            query.isTooShort() ->
                BadRequest("Query parameter 'q' is too short (it must have at least 3 characters)")

            query.startsWith(BANG) -> BangRequest(query)
            start < 0 -> BadRequest("Negative 'start' value: $start")
            start > rows -> BadRequest("'start' is greater than max_results")
            searchType == null -> BadRequest("No query parameter 'stype' provided")
            else -> RequestParam(query, searchType, rows, start, numPerPage)
        }
    } ?: BadRequest("No query parameter 'q' provided")
}

fun retrieveApiParam(queryParameters: Parameters, pathParameters: Parameters): IRequestParamResult {
    return queryParameters["q"]?.let { query ->
        getApiSearchTypeFrom(pathParameters)?.let { searchType ->
            val start: Int = queryParameters["start"]?.toInt() ?: 0
            val rows: Int = queryParameters["rows"]?.toInt() ?: MAX_ENTRIES
            val numPerPage: Int? = if (start > 0) rows - start else null

            when {
                query.isBlank() -> BadRequest("Query parameter 'q' is blank.")
                query.isTooShort() -> BadRequest("Query parameter 'q' is too short. It must have at least 3 characters.")
                query.startsWith(BANG) -> BadRequest("Bang request is not allowed in the REST API.")
                rows < 0 -> BadRequest("Negative 'rows' value: $rows. 'rows' must be positive or zero.")
                rows > MAX_ENTRIES -> BadRequest("'rows' exceed $MAX_ENTRIES entries")
                start < 0 -> BadRequest("Negative 'start' value: $rows. 'start' must be positive or zero.")
                start > rows -> BadRequest("'start' is greater than 'rows'")
                else -> RequestParam(query, searchType, rows, start, numPerPage)
            }
        } ?: BadRequest("Incorrect syntax: absent or incorrect search type")
    } ?: BadRequest("No query parameter 'q' provided")
}

private fun getSearchTypeFrom(parameters: Parameters): SearchType? {
    return parameters["stype"]?.let {
        when (it) {
            SearchType.RESEARCH.value -> SearchType.RESEARCH
            SearchType.BOOKS.value -> SearchType.BOOKS
            SearchType.MANGAS.value -> SearchType.MANGAS
            SearchType.ANIME.value -> SearchType.ANIME
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

private fun String.isTooShort(): Boolean {
    return length < MINIMUM_QUERY_LENGTH
}

fun SearchResponse.isEmpty(): Boolean {
    return totalResults == 0 && entries.isEmpty()
}

fun RequestParam.toQueryParam(uri: String, isRest: Boolean = false): QueryParam {
    return QueryParam(query, searchType, rows, start, numPerPage, uri, isRest)
}
