package io.gumichan01.gakusci.client.jikan

import io.gumichan01.gakusci.domain.utils.DateInterval

data class JikanMangaEntry(
    val title: String,
    val url: String,
    val publicationPeriod: DateInterval,
    val imageUrl: String? = null
)
