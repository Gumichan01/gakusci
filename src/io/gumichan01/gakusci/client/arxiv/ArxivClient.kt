package io.gumichan01.gakusci.client.arxiv

import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import io.gumichan01.gakusci.RateLimitViolationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URL

class ArxivClient {

    private val logger: Logger = LoggerFactory.getLogger(ArxivClient::class.java)
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


    suspend fun retrieveResults(query: String): List<ArxivResultEntry> {

        if (!rateLimiter.isRequestAllowed()) {
            throw RateLimitViolationException("Arxiv: Rate limit reached")
        }

        val url = arxivUrl.format(query)
        try {
            val reader: XmlReader = withContext(Dispatchers.IO) {
                XmlReader(URL(url))
            }

            val results: List<ArxivResultEntry> = SyndFeedInput().build(reader).entries.map { e ->
                ArxivResultEntry(
                    e.authors.map { a -> ArxivAuthor(a.name) },
                    e.title,
                    e.publishedDate,
                    e.link
                )
            }
            rateLimiter.reset()
            return results
        } catch (e: Exception) {

            logger.warn(e.message)
            return emptyList()
        }
    }
}