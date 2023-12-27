package io.gumichan01.gakusci.client.gutendex

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GutendexAuthor(val name: String)
