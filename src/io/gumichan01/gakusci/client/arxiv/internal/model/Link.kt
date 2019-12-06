package io.gumichan01.gakusci.client.arxiv.internal.model

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

@Root(strict = false)
data class Link(

    @field:Attribute(name = "href")
    @param:Attribute(name = "href")
    val href: String,

    @field:Attribute(name = "type", required = false)
    @param:Attribute(name = "type", required = false)
    val type: String?
)