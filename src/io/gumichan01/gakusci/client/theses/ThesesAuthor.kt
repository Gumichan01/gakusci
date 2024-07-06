package io.gumichan01.gakusci.client.theses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ThesesAuthor(
    @JsonProperty("nom") val lastName: String,
    @JsonProperty("prenom") val firstName: String
) {
    override fun toString() = "$firstName $lastName"
}
