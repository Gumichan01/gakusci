package io.gumichan01.gakusci.client.openlib

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class OpenLibrarySearchEntry(
    @JsonProperty("cover_i") val coverIndex: Int,
    @JsonProperty("title") val title: String,
    @JsonProperty("subtitle") val subtitle: String?,
    @JsonProperty("author_name") val authorNames: List<String>?,
    @JsonProperty("key") val key: String, // This is the path to the resource from https://openlibrary.org
    @JsonProperty("first_publish_year") val firstPublishYear: Int
) {
    fun authors(): String {
        return when {
            authorNames.isNullOrEmpty() -> "Unknown author"
            authorNames.size > 1 -> authorNames[0] + " et al."
            else -> authorNames[0]
        }
    }

    fun publishDate(): String {
        return if (firstPublishYear == 0) "n.d" else firstPublishYear.toString()
    }

    fun link(): String = "https://openlibrary.org$key"

    fun thumbnail(): String {
        return if (coverIndex == 0) {
            "/image/not-found.jpg"
        } else {
            "https://covers.openlibrary.org/b/id/${coverIndex}-M.jpg"
        }
    }
}
