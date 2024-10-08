package io.gumichan01.gakusci.controller

import io.gumichan01.gakusci.controller.utils.*
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.SearchResponse
import io.gumichan01.gakusci.domain.search.SearchQueryProcessor
import io.gumichan01.gakusci.domain.utils.SearchType
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.thymeleaf.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@FlowPreview
@ExperimentalCoroutinesApi
class WebController(private val searchQueryProcessor: SearchQueryProcessor) {

    private val logger: Logger = LoggerFactory.getLogger(WebController::class.java)

    suspend fun handleRequest(call: ApplicationCall) {
        when (val resultParam: IRequestParamResult = retrieveWebParam(call.request.queryParameters)) {
            is BadRequest -> call.respond(HttpStatusCode.BadRequest, resultParam.message)
            is BangRequest -> {
                when (val redirectUrl: String? = retrieveUrlRedirectOfService(resultParam.request)) {
                    null -> call.respond(HttpStatusCode.BadRequest, "Invalid or incomplete Bang request : ${resultParam.request}")
                    else -> call.respondRedirect(redirectUrl, false)
                }
            }

            is RequestParam -> {
                logger.trace(call.request.uri)
                val queryParam: QueryParam = resultParam.toQueryParam()
                call.respond(generateThymeleafContent(queryParam, searchQueryProcessor.proceed(queryParam)))
            }
        }
    }

    private fun retrieveUrlRedirectOfService(request: String): String? {
        val nbtokens = 2
        val tokens: List<String> = request.split(" ", limit = 2)
        if (tokens.size < nbtokens) {
            return null
        }
        val bangRequest: String = tokens[0]
        val query: String = tokens[1]
        return when (bangRequest) {
            // Research
            "!arxiv" -> "https://arxiv.org/search/?query=%s&searchtype=all"
            "!hal" -> "https://hal.science/search/index?q=%s"
            "!libgen" -> "https://libgen.is/search.php?req=%s"
            "!scihub" -> "https://sci-hub.st/%s"
            "!theses" -> "https://www.theses.fr/fr/?q=%s"
            "!thesis" -> "https://www.theses.fr/en/?q=%s"
            // Book
            "!goodreads" -> "https://www.goodreads.com/search?query=%s"
            "!openlib" -> "https://openlibrary.org/search?q=%s&mode=everything"
            "!penguin" -> "https://www.penguinrandomhouse.com/search/%s?q=%s".format(query, query)
            // Manga
            "!manga" -> "https://myanimelist.net/manga.php?q=%s"
            "!kitsumanga" -> "https://kitsu.io/manga?text=%s"
            // Anime
            "!anidb" -> "https://anidb.net/anime/?adb.search=%s"
            "!anime" -> "https://myanimelist.net/anime.php?q=%s"
            "!anilist" -> "https://anilist.co/search/anime?sort=SEARCH_MATCH&search=%s"
            "!kitsuanime" -> "https://kitsu.io/anime?text=%s"
            // Music
            "!discogs" -> "https://www.discogs.com/search?q=%s"
            "!freesound" -> "https://freesound.org/search/?q=%s"
            "!musicbrainz" -> "https://musicbrainz.org/search?type=artist&query=%s"
            "!opengameart" -> "https://opengameart.org/art-search?keys=%s"
            else -> null
        }?.format(query)
    }

    private fun generateThymeleafContent(queryParam: QueryParam, response: SearchResponse): ThymeleafContent {

        // resources/template/<template>.html
        val templateName: String = when (queryParam.searchType) {
            SearchType.RESEARCH -> "research" // research.html
            SearchType.BOOKS, SearchType.MANGAS, SearchType.ANIME -> "culture" // culture.html
            else -> throw IllegalStateException("Cannot create HTML template for ${queryParam.searchType}")
        }
        val numPerPage: Int = queryParam.rows
        val numberOfPaginatedResults: Int = response.totalResults
        val pageOffset: Int = numberOfPaginatedResults % numPerPage

        return ThymeleafContent(
            templateName, mapOf(
            "numFound" to response.totalResults,
            "entries" to response.entries,
            "query" to queryParam.query,
            "stype" to queryParam.searchType.value,
            "emptyEntries" to response.entries.isEmpty(),
            "pstart" to queryParam.start - numPerPage,
            "start" to queryParam.start,
            "nstart" to queryParam.start + numPerPage,
            "lastStart" to numberOfPaginatedResults - (if (pageOffset == 0) 10 else pageOffset),
            "numPerPage" to numPerPage,
            SearchType.RESEARCH.value to (queryParam.searchType == SearchType.RESEARCH),
            SearchType.BOOKS.value to (queryParam.searchType == SearchType.BOOKS),
            SearchType.MANGAS.value to (queryParam.searchType == SearchType.MANGAS),
            SearchType.ANIME.value to (queryParam.searchType == SearchType.ANIME)
        )
        )
    }
}