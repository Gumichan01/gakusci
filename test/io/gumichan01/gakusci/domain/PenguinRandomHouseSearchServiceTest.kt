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
}