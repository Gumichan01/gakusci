package io.gumichan01.gakusci.domain.model.entry

data class BookEntry(val entry: SimpleResultEntry, val thumbnailUrl: String): IResultEntry {
    override fun label() = entry.label
    override fun link() = entry.url

}