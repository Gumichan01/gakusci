package io.gumichan01.gakusci.client.jikan

class JikanMangaEntry(
    val title: String,
    val url: String,
    val publicationPeriod: DateInterval,
    val imageUrl: String? = null
) {

    fun label() = "$title (${publicationPeriod.startDate} - ${publicationPeriod.endDate ?: ""})"
}
