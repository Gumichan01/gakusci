package io.gumichan01.gakusci.client.arxiv.internal

import io.gumichan01.gakusci.client.arxiv.internal.model.Link
import java.time.LocalDate

object ArxivUtils {
    fun toDate(date: String): LocalDate {
        return LocalDate.parse(date.substringBefore('T'))
    }

    fun getWebsiteLink(links: List<Link>): Link {
        return links.first { link -> link.type == "text/html" }
    }
}