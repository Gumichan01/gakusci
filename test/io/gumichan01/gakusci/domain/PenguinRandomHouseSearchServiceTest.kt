package io.gumichan01.gakusci.domain

import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.service.IService
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import kotlin.test.Test

class PenguinRandomHouseSearchServiceTest {

    @Test
    fun `Send valid search request - returns results`() {
        val service: IService = PenguinRandomHouseSearchService()
        val result: ServiceResponse? = runBlocking { service.search(QueryParam("lorem", SearchType.BOOKS)) }
        Assertions.assertThat(result).isNotNull
    }

    @Test
    fun `Send valid search request but get no result - returns response with no entry`() {
        val service: IService = PenguinRandomHouseSearchService()
        val result: ServiceResponse? = runBlocking { service.search(QueryParam("9780140043204", SearchType.BOOKS)) }
        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result?.totalResults).isZero()
        Assertions.assertThat(result?.entries).isEmpty()
    }
}