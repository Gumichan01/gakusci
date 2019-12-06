package io.gumichan01.gakusci.client.arxiv.internal.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(strict = false)
data class Author(
    @field:Element(name = "name")
    @param:Element(name = "name")
    val name: String
)