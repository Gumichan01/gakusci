package io.gumichan01.gakusci.domain.service.jikan

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.jikan.JikanAnimeEntry
import io.gumichan01.gakusci.client.jikan.JikanAnimeResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.service.IService
import io.gumichan01.gakusci.domain.utils.SearchType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import kotlin.test.Test

class JikanAnimeServiceTest {

    private val mockkJikanCLient: IClient<JikanAnimeResponse> = mockk {
        coEvery { retrieveResults(QueryParam("lorem", SearchType.ANIME, )) } returns JikanAnimeResponse(
            listOf(JikanAnimeEntry("lorem ipsum", "", 1, ""))
        )
    }

    @Test
    fun `Jikan Manga Service, launch simple request - must return something`() {
        val jikan: IService = JikanAnimeService(mockkJikanCLient)
        val response: ServiceResponse? = runBlocking { jikan.search(QueryParam("lorem", SearchType.ANIME, )) }
        Assertions.assertThat(response).isNotNull
        Assertions.assertThat(response!!.totalResults).isEqualTo(1)
        Assertions.assertThat(response.entries[0].label()).containsIgnoringCase("lorem ipsum")
    }
}