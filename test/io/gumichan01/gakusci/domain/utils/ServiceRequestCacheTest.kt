package io.gumichan01.gakusci.domain.utils

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.model.ServiceResponse
import io.gumichan01.gakusci.domain.service.IService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test


internal class ServiceRequestCacheTest() {

    private val delegatedCache: Cache<String, ServiceResponse> = Caffeine.newBuilder().maximumSize(4L).build()
    private val serviceMockk = mockk<IService> {
        coEvery {
            search(QueryParam("loremi", SearchType.RESEARCH))
        } returns ServiceResponse(0, emptyList())
    }

    @Before
    fun onBefore() {
        delegatedCache.invalidateAll()
    }

    @Test
    fun `Put data in cache and check its value - expect same data`() {
        val cache = ServiceRequestCache(delegatedCache)
        val expectedValue = ServiceResponse(0, emptyList())
        cache.put("lorem", ServiceResponse(0, emptyList()))
        val value: ServiceResponse = cache.get("lorem") {
            ServiceResponse(42, emptyList())
        }
        assertThat(cache.getIfPresent("lorem")).isNotNull
        assertThat(value).isEqualTo(expectedValue)
        assertThat(value.totalResults).isZero
        assertThat(value.entries.size).isZero
    }

    @Test
    fun `Send a request to a service and cache it, check its value - expect same data`() {
        val service: IService = serviceMockk
        val cache = ServiceRequestCache(delegatedCache)
        val resp01: ServiceResponse = cache.get("loremi") {
            runBlocking { service.search(QueryParam("loremi", SearchType.RESEARCH)) }
        }
        val resp02: ServiceResponse = cache.get("loremi") {
            ServiceResponse(421, emptyList())
        }
        val value: ServiceResponse = cache.get("loremi") {
            ServiceResponse(42, emptyList())
        }
        assertThat(cache.getIfPresent("loremi")).isNotNull
        assertThat(value).isEqualTo(resp01)
        assertThat(value).isEqualTo(resp02)
    }
}