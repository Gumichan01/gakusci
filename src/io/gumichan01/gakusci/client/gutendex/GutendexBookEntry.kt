package io.gumichan01.gakusci.client.gutendex

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GutendexBookEntry(
    val id: Int,
    val title: String,
    val authors: List<GutendexAuthor>
)
