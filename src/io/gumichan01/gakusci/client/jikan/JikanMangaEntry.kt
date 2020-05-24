package io.gumichan01.gakusci.client.jikan

import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class JikanMangaEntry(
    val title: String,
    val url: String,
    val publicationPeriod: DateInterval,
    val imageUrl: String? = null
) {

    fun label(): String {
        val localStartDate: LocalDate = publicationPeriod.startDate.toLocalDate()
        val localEndDate: LocalDate? = publicationPeriod.endDate?.toLocalDate()
        return "$title (${localStartDate.year} - ${localEndDate?.year ?: ""})"
    }

    private fun Date.toLocalDate(): LocalDate {
        return toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }
}
