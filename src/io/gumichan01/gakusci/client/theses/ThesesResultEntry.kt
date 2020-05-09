package io.gumichan01.gakusci.client.theses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ThesesResultEntry(
    @JsonProperty("titre") val title: String,
    @JsonProperty("auteur") val author: String,
    @JsonProperty("status") val status: String,
    @JsonProperty("accessible") val available: String,
    @JsonProperty("dateSoutenance") val date: String
)
