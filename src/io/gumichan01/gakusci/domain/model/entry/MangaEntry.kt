package io.gumichan01.gakusci.domain.model.entry

import io.gumichan01.gakusci.client.jikan.DateInterval
import io.gumichan01.gakusci.domain.utils.toText
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

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

    @Deprecated("remove it")
    private fun textOfPublicationPeriod(): String {
        val localStartDate: LocalDate = publicationPeriod.startDate.toLocalDate()
        val localEndDate: LocalDate? = publicationPeriod.endDate?.toLocalDate()
        return when {
            localEndDate == null -> "(${localStartDate.year} - ...)"
            localStartDate.year == localEndDate.year -> "(${localEndDate.year})"
            else -> "(${localStartDate.year} - ${localEndDate.year})"
        }
    }

    @Deprecated("remove it")
    private fun Date.toLocalDate(): LocalDate {
        return toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }

    override fun link(): String = url
    override fun cover(): String = thumbnailUrl
}