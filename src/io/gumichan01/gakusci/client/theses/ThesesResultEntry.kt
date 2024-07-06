package io.gumichan01.gakusci.client.theses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.text.SimpleDateFormat
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class ThesesResultEntry(
    @JsonProperty("id") val id: String,
    @JsonProperty("titrePrincipal") val title: String,
    @JsonProperty("auteurs") val authors: List<ThesesAuthor>,
    @JsonProperty("status") val status: String,
    @JsonProperty("dateSoutenance") val dateTxt: String?
) {

    fun label(): String {
        val originalPattern = "dd/MM/yyyy"
        val iso8601Pattern = "yyyy-MM-dd"
        val date: Date = SimpleDateFormat(originalPattern).parse(dateTxt!!)
        val formattedDate: String = SimpleDateFormat(iso8601Pattern).format(date)
        return "$authors. $title. $formattedDate"
    }

    fun isPresented(): Boolean {
        return status.lowercase() == "soutenue"
    }

    fun link(): String {
        return "https://www.theses.fr/$id"
    }

}
