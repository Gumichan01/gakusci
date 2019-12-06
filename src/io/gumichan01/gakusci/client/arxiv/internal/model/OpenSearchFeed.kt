package io.gumichan01.gakusci.client.arxiv.internal.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "atom", strict = false)
data class OpenSearchFeed(
    @field:Element(name = "totalResults")
    @param:Element(name = "totalResults")
    val totalResults: Int,

    @field:Element(name = "startIndex")
    @param:Element(name = "startIndex")
    val startIndex: Int,

    @field:Element(name = "itemsPerPage")
    @param:Element(name = "itemsPerPage")
    val itemsPerPage: Int
)