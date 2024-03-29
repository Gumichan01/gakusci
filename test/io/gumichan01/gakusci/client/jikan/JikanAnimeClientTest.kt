package io.gumichan01.gakusci.client.jikan

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.SimpleQuery
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions

class JikanAnimeClientTest {

    //@Test
    fun `Jikan Client, test anime search - return results`() {
        val client: IClient<JikanAnimeResponse> = JikanAnimeClient()
        val response: JikanAnimeResponse? = runBlocking { client.retrieveResults(SimpleQuery("soul eater")) }
        Assertions.assertThat(response).isNotNull
        Assertions.assertThat(response!!.entries.size).isGreaterThan(0)
    }
}