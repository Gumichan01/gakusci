package io.gumichan01.gakusci.client.arxiv.internal.model

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "atom", strict = false)
data class ArxivFeed(
    @field:ElementList(name = "entry", inline = true)
    @param:ElementList(name = "entry", inline = true)
    val entries: List<Entry>
)