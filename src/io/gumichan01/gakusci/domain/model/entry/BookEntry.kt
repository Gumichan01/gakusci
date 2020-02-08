package io.gumichan01.gakusci.domain.model.entry

data class BookEntry(val label: String, val url: String, val thumbnailUrl: String) : IResultEntry {
    override fun label() = label
    override fun link(): String = url
}