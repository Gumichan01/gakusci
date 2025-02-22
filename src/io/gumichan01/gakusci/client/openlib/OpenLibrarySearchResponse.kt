package io.gumichan01.gakusci.client.openlib

import com.fasterxml.jackson.annotation.JsonProperty


/*
* NOTE The response format is experimental and may change in the future
*
* The response format contains two properties that has the same value and seems identical: "num_found" and numFound
* Schema :
* https://github.com/internetarchive/openlibrary/blob/00a05558c6d8e7bb770f4f2684664ad048531dac/conf/solr/conf/managed-schema.xml#L131-L225
*
* The schema is not guaranteed to be stable, but some field related to the book should be used without any concern.
*/
data class OpenLibrarySearchResponse(
    val start: Int,
    @JsonProperty("num_found") val numFound: Int,
    @JsonProperty("numFound") val numFoundBis: Int,
    @JsonProperty("q") val query: String,
    val numFoundExact: Boolean,
    val offset: Any?,
    val docs: List<OpenLibrarySearchEntry>?,
    @JsonProperty("documentation_url", required = false) val docUrl: String?
)
