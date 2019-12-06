package io.gumichan01.gakusci.client.arxiv.internal

import com.ouattararomuald.syndication.Atom
import io.gumichan01.gakusci.client.arxiv.internal.model.ArxivFeed
import io.gumichan01.gakusci.client.arxiv.internal.model.OpenSearchFeed

// TODO 2 - Proper implementation
// TODO 3 - integrate it into the arxiv client
interface ArxivRssReader {

    @Atom(returnClass = ArxivFeed::class)
    fun read(): ArxivFeed

    @Atom(returnClass = OpenSearchFeed::class)
    fun readOpenSearch(): OpenSearchFeed
}

//fun main() {
//    val arxivUrl = "http://export.arxiv.org/api/query?search_query=ocaml"
//
//    val syndicationReader = Syndication(arxivUrl)
//    val reader = syndicationReader.create(ArxivRssReader::class.java)
//    val openSearchFeed = reader.readOpenSearch()
//    val feed = reader.read()
//
//    println(openSearchFeed)
//    println("Found ${feed.entries.size} entries \n")
//    feed.entries.map { e -> println("${toDate(e.published).toString()} --- ${getWebsiteLink(e.links).href} ") }
//}