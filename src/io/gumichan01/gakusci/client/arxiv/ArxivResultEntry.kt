package io.gumichan01.gakusci.client.arxiv

import java.util.*

data class ArxivResultEntry(
    val authors: List<ArxivAuthor>,
    val title: String,
    val publishedDate: Date,
    val link: String
) {
    fun label(): String = "$authors. $title. $publishedDate"
}