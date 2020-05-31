package io.gumichan01.gakusci.domain.model.entry

import io.gumichan01.gakusci.domain.utils.DateInterval
import io.gumichan01.gakusci.domain.utils.toText

data class MangaEntry(
    val title: String,
    val publicationPeriod: DateInterval,
    val url: String,
    val thumbnailUrl: String
) : IResultEntry {
    override fun label(): String {
        return StringBuilder()
            .append("$title ")
            .append(publicationPeriod.toText()).toString()
    }

    override fun link(): String = url
    override fun cover(): String = thumbnailUrl
}