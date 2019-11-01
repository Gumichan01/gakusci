package io.gumichan01.gakusci.client.arxiv

import java.text.SimpleDateFormat
import java.util.*

data class ArxivResultEntry(
    val authors: List<ArxivAuthor>,
    val title: String,
    val publishedDate: Date,
    val link: String
) {
    fun label(): String {
        val pattern = "yyyy"
        val formattedDate: String = SimpleDateFormat(pattern).format(publishedDate)
        return "$authors. $title. $formattedDate"
    }
}