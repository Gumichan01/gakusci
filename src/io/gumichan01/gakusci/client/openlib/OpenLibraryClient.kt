package io.gumichan01.gakusci.client.openlib

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.domain.model.QueryParam

// TODO Implement 'OpenLibraryResponse'
// TODO openLibClient must be IClient<OpenLibraryResponse>
class OpenLibraryClient : IClient<Any> {

    // NOTE This URL refers to an experimental Open Library API, so this class must be considered experimental
    private val openLibUrl = "https://openlibrary.org/search.json?q=%s"

    override suspend fun retrieveResults(queryParam: QueryParam): Any? {
        val url = openLibUrl.format(queryParam.query)
        return retrieveData(url)
    }

    private fun retrieveData(url: String): Any? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
