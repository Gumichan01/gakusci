package io.gumichan01.gakusci.domain.model.entry

data class AnimeEntry(
    val title: String,
    val nbEpisodes: Int?,
    val url: String,
    val thumbnailUrl: String
) : IResultEntry {
    override fun label(): String {
        return StringBuilder()
            .append(title)
            .append(if (nbEpisodes != null) " ($nbEpisodes eps)" else "").toString()
    }

    override fun link(): String = url
    override fun cover(): String = thumbnailUrl
}