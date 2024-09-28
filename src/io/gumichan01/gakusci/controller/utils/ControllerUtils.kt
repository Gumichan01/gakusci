package io.gumichan01.gakusci.controller.utils

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.utils.SearchType
import io.ktor.http.*

private const val MAX_ENTRIES = 10000
private const val NUM_PER_WEBPAGE = 10
private const val MINIMUM_QUERY_LENGTH = 3
private const val BANG = '!'

fun retrieveWebParam(queryParameters: Parameters): IRequestParamResult {
    return queryParameters["q"]?.let { query ->
        val start: Int = queryParameters["start"]?.toInt() ?: 0
        val searchType: SearchType? = getSearchTypeFrom(queryParameters)
        val rows: Int = NUM_PER_WEBPAGE

        when {
            query.isBlank() -> BadRequest("Query parameter 'q' is blank")
            query.isTooShort() ->
                BadRequest("Query parameter 'q' is too short (it must have at least 3 characters)")

            query.startsWith(BANG) -> BangRequest(query)
            start < 0 -> BadRequest("Negative 'start' value: $start")
            searchType == null -> BadRequest("No query parameter 'stype' provided")
            else -> RequestParam(query, searchType, rows, start)
        }
    } ?: BadRequest("No query parameter 'q' provided")
}

fun retrieveApiParam(queryParameters: Parameters, pathParameters: Parameters): IRequestParamResult {
    return queryParameters["q"]?.let { query ->
        getSearchTypeFrom(pathParameters)?.let { searchType ->
            val start: Int = queryParameters["start"]?.toInt() ?: 0
            val rows: Int = queryParameters["rows"]?.toInt() ?: MAX_ENTRIES

            when {
                query.isBlank() -> BadRequest("Query parameter 'q' is blank.")
                query.isTooShort() -> BadRequest("Query parameter 'q' is too short. It must have at least 3 characters.")
                query.startsWith(BANG) -> BadRequest("Bang request is not allowed in the REST API.")
                rows < 0 -> BadRequest("Negative 'rows' value: $rows. 'rows' must be positive or zero.")
                rows > MAX_ENTRIES -> BadRequest("'rows' exceed $MAX_ENTRIES entries")
                start < 0 -> BadRequest("Negative 'start' value: $rows. 'start' must be positive or zero.")
                else -> RequestParam(query, searchType, rows, start)
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
            SearchType.MUSIC.value -> SearchType.MUSIC
            else -> null
        }
    }
}

private fun String.isTooShort(): Boolean {
    return length < MINIMUM_QUERY_LENGTH
}

fun RequestParam.toQueryParam(): QueryParam {
    return QueryParam(query, searchType, rows, start)
}
