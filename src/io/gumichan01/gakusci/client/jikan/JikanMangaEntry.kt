package io.gumichan01.gakusci.client.jikan

class JikanMangaEntry(
    val title: String,
    val url: String,
    val imageUrl: String,
    val publicationPeriod: DateInterval
) {

    fun label() = "$title (${publicationPeriod.startDate} - ${publicationPeriod.endDate ?: ""})"
}
