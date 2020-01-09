package io.gumichan01.gakusci.client.openlib

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.domain.model.QueryParam
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get

class OpenLibraryBookClient: IClient<String> {
    private val openLibrarySearchUrl = "http://openlibrary.org/api/books?bibkeys=%s&format=json"

    override suspend fun retrieveResults(queryParam: QueryParam): String? {
        // TODO OpenLibraryBookClient 1. Check valid ISBN/LCCN/OCLC number
        // TODO OpenLibraryBookClient 2. Deserialize from JSON to OpenLibraryBookResponse
        return HttpClient(Apache).use { it.get(openLibrarySearchUrl.format(queryParam.query)) }
    }
}