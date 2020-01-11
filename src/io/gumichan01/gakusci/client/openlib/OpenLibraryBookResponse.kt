package io.gumichan01.gakusci.client.openlib

import com.fasterxml.jackson.annotation.JsonProperty

data class OpenLibraryBookResponse(
    @JsonProperty("bib_key") val bibKey: String,
    @JsonProperty("info_url") val infoUrl: String,
    @JsonProperty("preview") val preview: String,
    @JsonProperty("preview_url") val previewUrl: String,
    @JsonProperty("thumbnail_url") val thumbnailUrl: String?
)