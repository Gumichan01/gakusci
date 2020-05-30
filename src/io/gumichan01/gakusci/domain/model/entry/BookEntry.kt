package io.gumichan01.gakusci.domain.model.entry

// TODO Redefine the book entry (title, author)
data class BookEntry(
    val title: String? = null,
    val author: String? = null,
    val date: String? = null,
    val bibKey: String? = null,
    val url: String,
    val thumbnailUrl: String
) : IResultEntry {
    override fun label(): String {
        return StringBuilder()
            .append("$title ,")
            .append(if (author != null) " $author," else "")
            .append(" $date").toString()
    }

    override fun link(): String = url
    override fun cover(): String = thumbnailUrl
}