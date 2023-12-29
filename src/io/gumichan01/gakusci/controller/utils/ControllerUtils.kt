package io.gumichan01.gakusci.controller.utils

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.SearchResponse
import io.gumichan01.gakusci.domain.utils.SearchType
import io.ktor.http.*

const val MAX_ENTRIES = 2000
const val MINIMUM_QUERY_LENGTH = 3
const val BANG = '!'

fun retrieveWebParam(queryParameters: Parameters): IRequestParamResult {
    return queryParameters["q"]?.let { query ->
        val defaultRows = 100
        val numPerPage = 10
        val start : Int = queryParameters["start"]?.toInt() ?: 0
        val searchType: SearchType? = getSearchTypeFrom(queryParameters)
        // The webapp must not retrieve more than 2000 entries, for the sake of performance
        val rows : Int = if (start + numPerPage > defaultRows) {
            if (start * 2 > MAX_ENTRIES) MAX_ENTRIES else start * 2
        } else defaultRows

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
            val start: Int? = queryParameters["start"]?.toInt()
            val rows: Int? = queryParameters["max_results"]?.toInt()
            val numPerPage: Int? = queryParameters["num_per_page"]?.toInt()

            when {
                query.isBlank() -> BadRequest("Query parameter 'q' is blank")
                query.isTooShort() ->
                    BadRequest("Query parameter 'q' is too short (it must have at least 3 characters)")
                query.startsWith(BANG) -> BadRequest("Bang request is not allowed in the REST API")
                rows != null && rows < 0 -> BadRequest("Negative 'max_results' value: $rows")
                rows != null && rows > MAX_ENTRIES ->
                    BadRequest("Cannot request 'max_results' greater than $MAX_ENTRIES")
                start != null -> {
                    RestApiQueryParameters(query, searchType, rows, start, numPerPage).run {
                        retrieveAndCheckRestApiQueryParameters(this)
                    }
                }
                numPerPage != null -> BadRequest("Cannot set 'num_per_page' with no 'start' value")
                else -> RequestParam(query, searchType, rows ?: 10, 0, null)
            }
        } ?: BadRequest("Incorrect syntax: absent or incorrect search type")
    } ?: BadRequest("No query parameter 'q' provided")
}

private fun retrieveAndCheckRestApiQueryParameters(queryParameters: RestApiQueryParameters): IRequestParamResult {
    return queryParameters.run {
        when {
            start < 0 -> BadRequest("Negative 'start' value: $start")
            numPerPage == null -> BadRequest("Cannot set 'start' with no 'num_per_page'")
            numPerPage < 0 -> BadRequest("Negative 'num_per_page' value: $start")
            rows == null -> BadRequest("Cannot set 'start' with no 'max_results'")
            start > rows -> BadRequest("'start' is greater than 'max_results'")
            numPerPage > rows -> BadRequest("'num_per_page' is greater than 'max_results'")
            else -> RequestParam(query, searchType, rows, start, numPerPage)
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

fun RequestParam.toQueryParam(uri: String): QueryParam {
    return QueryParam(query, searchType, rows, start, numPerPage, uri)
}
