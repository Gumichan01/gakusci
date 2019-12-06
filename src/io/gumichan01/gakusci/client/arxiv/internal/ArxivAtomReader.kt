package io.gumichan01.gakusci.client.arxiv.internal

import com.ouattararomuald.syndication.Atom
import io.gumichan01.gakusci.client.arxiv.internal.model.ArxivFeed

interface ArxivAtomReader {
    @Atom(returnClass = ArxivFeed::class)
    fun readAtom(): ArxivFeed
}