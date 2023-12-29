package io.gumichan01.gakusci.client.jikan

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions

class JikanMangaClientTest {

    //@Test
    fun `Jikan Client, test manga search - return results`() {
        val client: IClient<JikanMangaResponse> = JikanMangaClient()
        val response: JikanMangaResponse? = runBlocking { client.retrieveResults(QueryParam("soul eater", SearchType.MANGAS, )) }
        Assertions.assertThat(response).isNotNull
        Assertions.assertThat(response!!.entries.size).isGreaterThan(0)
    }
}