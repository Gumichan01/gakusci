package io.gumichan01.gakusci.client.arxiv

import com.ouattararomuald.syndication.Syndication
import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket4j
import io.github.bucket4j.Refill
import io.github.bucket4j.local.LocalBucket
import io.gumichan01.gakusci.client.arxiv.internal.ArxivAtomReader
import io.gumichan01.gakusci.client.arxiv.internal.ArxivUtils
import io.gumichan01.gakusci.client.arxiv.internal.model.ArxivFeed
import java.time.Duration

class ArxivClient {

    private val arxivUrl = "https://export.arxiv.org/api/query?search_query=%s"
    private val rateLimiter = createLimiter()

    private fun createLimiter(): LocalBucket {
        return Bucket4j.builder()
            .addLimit(Bandwidth.classic(1, Refill.greedy(1L, Duration.ofSeconds(3))))
            .build()
    }

    fun retrieveResults(query: String): ArxivResponse? {
        return if (rateLimiter.tryConsume(1L)) {
            val url: String = arxivUrl.format(query)
            val arxivFeed: ArxivFeed = Syndication(url).create(ArxivAtomReader::class.java).readAtom()
            ArxivResponse(arxivFeed.totalResults, arxivFeed.startIndex, arxivFeed.results())
        } else null
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