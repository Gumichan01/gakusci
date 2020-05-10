package io.gumichan01.gakusci.client.theses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.text.SimpleDateFormat
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class ThesesResultEntry(
    @JsonProperty("num") val num: String,
    @JsonProperty("titre") val title: String,
    @JsonProperty("auteur") val author: String,
    @JsonProperty("status") val status: String,
    @JsonProperty("accessible") val available: String,
    @JsonProperty("dateSoutenance") val date: Date
) {

    fun label(): String {
        val pattern = "yyyy-MM-dd"
        val formattedDate: String = SimpleDateFormat(pattern).format(date)
        return "$author. $title. $formattedDate"
    }

    fun hasAccess(): Boolean {
        return available.toLowerCase() == "oui"
    }

    fun isPresented(): Boolean {
        return status.toLowerCase() == "soutenue"
    }

    fun link(): String {
        return "https://www.theses.fr/$id"
    }

}
