package io.gumichan01.gakusci.domain.model.entry

// TODO Redefine the book entry (title, author)
data class BookEntry(val label: String, val url: String, val thumbnailUrl: String) : IResultEntry {
    override fun label(): String = label
    override fun link(): String = url
    override fun cover(): String = thumbnailUrl
}