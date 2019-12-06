package io.gumichan01.gakusci.client.arxiv.internal

import io.gumichan01.gakusci.client.arxiv.internal.model.Link
import java.text.SimpleDateFormat
import java.util.*

object ArxivUtils {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    fun toDate(date: String): Date {
        return dateFormat.parse(date)
    }

    fun getWebsiteLink(links: List<Link>): Link {
        return links.first { link -> link.type == "text/html" }
    }
}