package io.gumichan01.gakusci.client.jikan

data class JikanAnimeEntry(
    val title: String,
    val url: String,
    val episodes: Int? = null,
    val imageUrl: String? = null
)
