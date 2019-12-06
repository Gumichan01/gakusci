package io.gumichan01.gakusci.client.arxiv.internal.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(strict = false)
data class Entry(
    @field:ElementList(name = "author", inline = true)
    @param:ElementList(name = "author", inline = true)
    val authors: List<Author>,

    @field:Element(name = "title")
    @param:Element(name = "title")
    val title: String,

    @field:Element(name = "published")
    @param:Element(name = "published")
    val published: String,

    @field:ElementList(name = "link", inline = true)
    @param:ElementList(name = "link", inline = true)
    val links: List<Link>
)