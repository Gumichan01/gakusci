package io.gumichan01.gakusci.client.gutendex

data class GutendexResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<GutendexBookEntry>
)
