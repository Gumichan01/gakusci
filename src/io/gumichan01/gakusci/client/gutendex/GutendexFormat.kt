package io.gumichan01.gakusci.client.gutendex

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class GutendexFormat(@JsonProperty("text/html") val html: String?, @JsonProperty("image/jpeg") val jpeg: String?)

