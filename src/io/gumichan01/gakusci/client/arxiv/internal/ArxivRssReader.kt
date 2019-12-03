package io.gumichan01.gakusci.client.arxiv.internal

import com.ouattararomuald.syndication.Atom
import com.ouattararomuald.syndication.Syndication
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.time.LocalDate

// TODO 2 - Proper implementation
// TODO 3 - integrate it into the arxiv client
interface ArxivRssReader {

    @Atom(returnClass = ArxivFeed::class)
    public fun read(): ArxivFeed

    @Atom(returnClass = OpenSearchFeed::class)
    public fun readOpenSearch(): OpenSearchFeed
}

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

@Root(name = "atom", strict = false)
data class ArxivFeed(
    @field:ElementList(name = "entry", inline = true)
    @param:ElementList(name = "entry", inline = true)
    val entries: List<Entry>
)

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

@Root(strict = false)
data class Author(
    @field:Element(name = "name")
    @param:Element(name = "name")
    val name: String
)

@Root(strict = false)
data class Link(

    @field:Attribute(name = "href")
    @param:Attribute(name = "href")
    val href: String,

    @field:Attribute(name = "type", required = false)
    @param:Attribute(name = "type", required = false)
    val type: String?
)

fun toDate(date: String): LocalDate? {
    return LocalDate.parse(date.substringBefore('T'))
}

fun getWebsiteLink(links: List<Link>): Link {
    return links.first { link -> link.type == "text/html" }
}

fun main() {
    val arxivUrl = "http://export.arxiv.org/api/query?search_query=ocaml"

    val syndicationReader = Syndication(arxivUrl)
    val reader = syndicationReader.create(ArxivRssReader::class.java)
    val openSearchFeed = reader.readOpenSearch()
    val feed = reader.read()

    println(openSearchFeed)
    println("Found ${feed.entries.size} entries \n")
    feed.entries.map { e -> println("${toDate(e.published).toString()} --- ${getWebsiteLink(e.links).href} ")}
}