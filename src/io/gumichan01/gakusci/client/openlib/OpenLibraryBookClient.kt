package io.gumichan01.gakusci.client.openlib

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.utils.*
import io.gumichan01.gakusci.domain.model.QueryParam
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get

class OpenLibraryBookClient : IClient<List<OpenLibraryBookResponse>> {
    private val openLibrarySearchUrl = "http://openlibrary.org/api/books?bibkeys=%s&format=json"

    override suspend fun retrieveResults(queryParam: QueryParam): List<OpenLibraryBookResponse>? {
        // TODO OpenLibraryBookClient 2. Deserialize from JSON to OpenLibraryBookResponse
        // TODO don't call the external servie if the formmated book number is blank
        return HttpClient(Apache).use { it.get<String>(openLibrarySearchUrl.format(formatBookNumber(queryParam.query))) }
            .fromJson()
    }

    private fun formatBookNumber(bookNumber: String): String {
        return StringBuilder().run {
            when {
                isValidISBN(bookNumber) -> append("ISBN:${normalizeIsbn(bookNumber)}")
                // A valid OCLC number a valid LCCN
                isValidOCLC(bookNumber) -> append("OCLC:$bookNumber,LCCN:${normalizeLccn(bookNumber)}")
                isValidLCCN(normalizeLccn(bookNumber)) -> append("LCCN:${normalizeLccn(bookNumber)}")
                else -> this
            }
        }.toString()
    }

    private fun String.fromJson(): List<OpenLibraryBookResponse> {
        return jacksonObjectMapper().readValue<Map<String, OpenLibraryBookResponse>>(
            this,
            object : TypeReference<Map<String, OpenLibraryBookResponse>>() {}).values.toList()
    }
}