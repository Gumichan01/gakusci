package io.gumichan01.gakusci.client.gutendex

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class GutendexBookEntry(
    val id: Int,
    val title: String,
    val authors: List<GutendexAuthor>,
    @JsonProperty("formats")
    val format: GutendexFormat
) {
    fun link(): String = format.html ?: ""
    fun thumbnail(): String = format.jpeg ?: "/image/not-found.jpg"
    fun isAccessible(): Boolean = format.html != null && format.jpeg != null
}
