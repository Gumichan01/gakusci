package io.gumichan01.gakusci.client.hal

import com.fasterxml.jackson.annotation.JsonProperty

data class HalResultEntry(
    val docid: Int,
    @JsonProperty("label_s") val label: String,
    @JsonProperty("uri_s") val uri: String
)