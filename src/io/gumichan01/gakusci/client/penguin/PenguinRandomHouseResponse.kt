package io.gumichan01.gakusci.client.penguin

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class PenguinRandomHouseResponse(
    @JsonProperty("isbn") val isbn: String,
    @JsonProperty("authorweb") val author: String,
    @JsonProperty("titleweb") val title: String,
    @JsonProperty("onsaledate") val publishDate: String
) {
    fun label(): String = "$title, $author, ${publishDate.split("/").last()}"
    fun link(): String = "https://penguinrandomhouse.com/search/site?q=$isbn"
    fun thumbnail(): String = "https://images1.penguinrandomhouse.com/cover/$isbn"
}
