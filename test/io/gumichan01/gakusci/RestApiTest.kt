package io.gumichan01.gakusci

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

@FlowPreview
@KtorExperimentalAPI
@ExperimentalCoroutinesApi
class RestApiTest {
    @Test
    fun `test REST Web-service API v1 search, normal case - return OK and non empty content`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/api/v1/researches/?q=lorem").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
                assertThat(response.content).isNotBlank()
            }
        }
    }

    @Test
    fun `test REST Web-service API v1 search, no query - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/api/v1/researches/").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `test REST Web-service API v1 search, query with start but no num_per_page - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/api/v1/researches/?q=a&start=0").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `test REST Web-service API v1 search, query with num_per_page but no start - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/api/v1/researches/?q=a&num_per_page=2").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `test REST Web-service API v1 search, query with start but no max_results - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/api/v1/researches/?q=a&start=0").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `test REST Web-service API v1 search, query with pagination (start, num_per_page, max_results) - return ok`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/api/v1/researches/?q=a&start=0&num_per_page=4&max_results=16").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
            }
        }
    }

    @Test
    fun `test REST Web-service API v1 search, query with start greater than max_results - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/api/v1/researches/?q=a&max_results=10&start=100&num_per_page=2").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `test REST Web-service API v1 search, query with negative start value - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/api/v1/researches/?q=a&start=-10&max_results=16&num_per_page=2").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `test REST Web-service API v1 search, pagination but with negative max_results - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/api/v1/researches/?q=b&max_results=-1&start=0&num_per_page=2").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `test REST Web-service API v1 search, negative max_results without pagination - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/api/v1/researches/?q=b&max_results=-1").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `test REST Web-service API v1 search, negative num_per_page - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/api/v1/researches/?q=b&max_results=10&start=0&num_per_page=-1").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `test REST Web-service API v1 search, query with numPerPage greater than max_results - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/api/v1/researches/?q=b&max_results=10&start=1&num_per_page=100000").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }
}