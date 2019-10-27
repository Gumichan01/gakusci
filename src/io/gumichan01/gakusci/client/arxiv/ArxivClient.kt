package io.gumichan01.gakusci.client.arxiv

import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.net.URL

// TODO Add a rate limiter https://arxiv.org/help/api/tou#rate-limits
class ArxivClient {

    private val logger = LoggerFactory.getLogger(ArxivClient::class.java)
    private val arxivUrl = "https://export.arxiv.org/api/query?search_query=%s"

    suspend fun retrieveResults(query: String): List<ArxivResultEntry> {
        val url = arxivUrl.format(query)
        try {
            val reader: XmlReader = withContext(Dispatchers.IO) {
                XmlReader(URL(url))
            }

            return SyndFeedInput().build(reader).entries.map { e ->
                ArxivResultEntry(
                    e.authors.map { a -> ArxivAuthor(a.name) },
                    e.title,
                    e.publishedDate,
                    e.link
                )
            }
        } catch (e: Exception) {
            logger.warn(e.message)
            return emptyList()
        }
    }
}