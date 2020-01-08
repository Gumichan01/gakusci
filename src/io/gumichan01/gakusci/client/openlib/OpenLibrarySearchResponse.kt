package io.gumichan01.gakusci.client.openlib

import com.fasterxml.jackson.annotation.JsonProperty


/*
* NOTE The response format is experimental and may change in the future
*
* The response format contains two properties that has the same value and seems identical: "num_found" and numFound
* The documentation (1st december, 2016) does not seems up-to-date.
*/
data class OpenLibrarySearchResponse(
    val start: Int,
    @JsonProperty("num_found") val numFound: Int,
    @JsonProperty("numFound") val numFoundBis: Int,
    val docs: List<OpenLibrarySearchEntry>?
)
