package io.gumichan01.gakusci.client.openlib

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.domain.model.QueryParam
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get
import org.slf4j.Logger
import org.slf4j.LoggerFactory

// TODO Implement 'OpenLibraryResponse'
// TODO openLibClient must be IClient<OpenLibraryResponse>
class OpenLibraryClient : IClient<String> {

    private val logger: Logger = LoggerFactory.getLogger(OpenLibraryClient::class.java)
    // NOTE This URL refers to an experimental Open Library API, so this class must be considered experimental
    private val openLibUrl = "https://openlibrary.org/search.json?q=%s"

    override suspend fun retrieveResults(queryParam: QueryParam): String? {
        val url = openLibUrl.format(queryParam.query)
        return retrieveData(url)
    }

    private suspend fun retrieveData(url: String): String? {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val client = HttpClient(Apache)

        return try {
            client.use { it.get(url) }
        } catch (e: Exception) {
            logger.trace(e.message)
            if (logger.isTraceEnabled) {
                e.printStackTrace()
            }
            null
        } finally {
            client.close()
        }
    }
}
