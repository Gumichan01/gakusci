package io.gumichan01.gakusci.domain.service

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.jikan.DateInterval
import io.gumichan01.gakusci.client.jikan.JikanMangaEntry
import io.gumichan01.gakusci.client.jikan.JikanMangaResponse
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.utils.SearchType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import java.util.*
import kotlin.test.Test

class JikanMangaServiceTest {

    private val mockkJikanCLient: IClient<JikanMangaResponse> = mockk {
        coEvery { retrieveResults(QueryParam("lorem", SearchType.BOOKS)) } returns JikanMangaResponse(
            listOf(
                JikanMangaEntry(
                    title = "lorem ipsum",
                    url = "",
                    imageUrl = "",
                    publicationPeriod = DateInterval(Calendar.getInstance().time)
                )
            )
        )
    }

    @Test
    fun `Jikan Manga Service, launch simple request - must return something`() {
        val jikan: IService = JikanMangaService(mockkJikanCLient)
        val response: ServiceResponse? = runBlocking { jikan.search(QueryParam("lorem", SearchType.BOOKS)) }
        Assertions.assertThat(response).isNotNull
        Assertions.assertThat(response!!.totalResults).isEqualTo(1)
        Assertions.assertThat(response.entries[0].label()).containsIgnoringCase("lorem ipsum")
    }
}