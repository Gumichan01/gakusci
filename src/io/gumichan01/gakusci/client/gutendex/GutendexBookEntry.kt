package io.gumichan01.gakusci.client.gutendex

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.gumichan01.gakusci.domain.utils.defaultThumbnailLink

@JsonIgnoreProperties(ignoreUnknown = true)
data class GutendexBookEntry(
    val id: Int,
    val title: String,
    val authors: List<GutendexAuthor>,
    @JsonProperty("formats")
    val format: GutendexFormat
) {
    fun link(): String = format.html ?: ""
    fun thumbnail(): String = format.jpeg ?: defaultThumbnailLink()
    fun isAccessible(): Boolean = format.html != null && format.jpeg != null
}
