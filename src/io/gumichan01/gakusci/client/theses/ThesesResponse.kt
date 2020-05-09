package io.gumichan01.gakusci.client.theses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ThesesResponse(
    @JsonProperty("response") val body: ThesesResponseBody
)