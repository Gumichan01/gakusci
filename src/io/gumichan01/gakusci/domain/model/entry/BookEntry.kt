package io.gumichan01.gakusci.domain.model.entry

data class BookEntry(
    val title: String? = null,
    val author: String? = null,
    val date: String? = null,
    val bibKey: String? = null,
    val url: String,
    val thumbnailUrl: String
) : IResultEntry {
    override fun label(): String {
        return StringBuilder().apply {
            if (title != null)
                append(title)
            if (author != null)
                append(", $author")
            if (date != null)
                append(" ($date)")
            if (bibKey != null)
                append(" - $bibKey")
        }.toString()
    }

    override fun link(): String = url
    override fun cover(): String = thumbnailUrl
}