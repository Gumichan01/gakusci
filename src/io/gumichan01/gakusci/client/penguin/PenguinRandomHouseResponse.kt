package io.gumichan01.gakusci.client.penguin

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class PenguinRandomHouseResponse(
    @JsonProperty("isbn") val isbn: String,
    @JsonProperty("authorweb") val author: String,
    @JsonProperty("titleweb") val title: String,
    @JsonProperty("onsaledate") val publishDate: String
)