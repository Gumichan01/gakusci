package io.gumichan01.gakusci.client.arxiv

import com.ouattararomuald.syndication.Syndication
import io.gumichan01.gakusci.client.arxiv.internal.ArxivAtomReader
import io.gumichan01.gakusci.client.arxiv.internal.ArxivUtils
import io.gumichan01.gakusci.client.arxiv.internal.model.ArxivFeed
import io.gumichan01.gakusci.client.exception.RateLimitViolationException

class ArxivClient {

    private val arxivUrl = "https://export.arxiv.org/api/query?search_query=%s"

    /**
     * This quick-and-dirty implementation of the rate limiter is specific to the Arxiv client.
     * See this link: https://arxiv.org/help/api/tou#rate-limits
     * Some other services may need something similar, but YAGNI
     */
    private val rateLimiter = object {
        private val TIME_LIMIT_BETWEEN_REQUESTS_IN_MILLISECONDS = 3000L
        private var requestTimestamp = 0L

        fun reset() {
            requestTimestamp = System.currentTimeMillis()
        }

        fun isRequestAllowed(): Boolean {
            return (System.currentTimeMillis() - requestTimestamp) > TIME_LIMIT_BETWEEN_REQUESTS_IN_MILLISECONDS
        }
    }

    fun retrieveResults(query: String): ArxivResponse {
        if (!rateLimiter.isRequestAllowed()) {
            throw RateLimitViolationException("Arxiv: Rate limit reached")
        }

        val url = arxivUrl.format(query)
        // TODO Find a library that handles rate limiting properly
        val arxivFeed: ArxivFeed = Syndication(url).create(ArxivAtomReader::class.java).readAtom()
        rateLimiter.reset()
        return ArxivResponse(arxivFeed.totalResults, arxivFeed.startIndex, arxivFeed.results())
    }

    fun ArxivFeed.results(): List<ArxivResultEntry> {
        return entries.map { e ->
            ArxivResultEntry(
                e.authors.map { a -> ArxivAuthor(a.name) },
                e.title,
                ArxivUtils.toDate(e.published),
                ArxivUtils.getWebsiteLink(e.links).href
            )
        }
    }
}