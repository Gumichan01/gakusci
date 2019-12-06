package io.gumichan01.gakusci.client.arxiv.internal

import com.ouattararomuald.syndication.Atom
import io.gumichan01.gakusci.client.arxiv.internal.model.ArxivFeed
import io.gumichan01.gakusci.client.arxiv.internal.model.OpenSearchFeed

// TODO 2 - Proper implementation
// TODO 3 - integrate it into the arxiv client
interface ArxivAtomReader {

    @Atom(returnClass = ArxivFeed::class)
    fun read(): ArxivFeed

    @Atom(returnClass = OpenSearchFeed::class)
    fun readOpenSearch(): OpenSearchFeed
}