package io.gumichan01.gakusci.client.theses

import com.fasterxml.jackson.annotation.JsonProperty

data class ThesesResponseBody(
    @JsonProperty("numFound") val numFound: Int,
    @JsonProperty("start") val start: Int,
    @JsonProperty("docs") val docs: List<ThesesResultEntry>
)
