package io.gumichan01.gakusci.client.hal

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class HalResponseBody(val numFound: Int, val start: Int, val docs: List<HalResultEntry>?)