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
    fun `test REST Web-service API v1 search, blank query - returns Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/api/v1/researches/?q=     ").apply {
                with(response) {
                    assertThat(status()).isEqualTo(HttpStatusCode.BadRequest)
                }
            }
        }
    }

    @Test
    fun `webapp, query too short (less than 3 characters) - returns Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/api/v1/researches/?q=a").apply {
                with(response) {
                    assertThat(status()).isEqualTo(HttpStatusCode.BadRequest)
                }
            }
        }
    }

    @Test
    fun `test REST Web-service API v1 search, query with start but no num_per_page - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/api/v1/researches/?q=abcd&start=0").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `test REST Web-service API v1 search, query with num_per_page but no start - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/api/v1/researches/?q=abcd&num_per_page=2").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `test REST Web-service API v1 search, query with start but no max_results - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/api/v1/researches/?q=abcd&start=0").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `test REST Web-service API v1 search, query with pagination (start, num_per_page, max_results) - return ok`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/api/v1/researches/?q=abcd&start=0&num_per_page=4&max_results=16").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
            }
        }
    }

    @Test
    fun `test REST Web-service API v1 search, query with start greater than max_results - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/api/v1/researches/?q=abcda&max_results=10&start=100&num_per_page=2").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `test REST Web-service API v1 search, query with negative start value - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/api/v1/researches/?q=abcda&start=-10&max_results=16&num_per_page=2").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `test REST Web-service API v1 search, pagination but with negative max_results - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/api/v1/researches/?q=abcdb&max_results=-1&start=0&num_per_page=2").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `test REST Web-service API v1 search, negative max_results without pagination - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/api/v1/researches/?q=abcdb&max_results=-1").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `test REST Web-service API v1 search, negative num_per_page - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/api/v1/researches/?q=abcdb&max_results=10&start=0&num_per_page=-1").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `test REST Web-service API v1 search, query with numPerPage greater than max_results - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(
                HttpMethod.Get,
                "/api/v1/researches/?q=babcd&max_results=10&start=1&num_per_page=100000"
            ).apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `test REST API v1, max_results greater than the maximum number of entries allowed by the app - Bad request`() {
        withTestApplication({ gakusciModule() }) {
            val bigStartValue = 1 shl 20
            handleRequest(
                HttpMethod.Get,
                "/api/v1/researches/?q=aabcd&start=0&max_results=$bigStartValue&num_per_page=2"
            ).apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `test REST API v1, max_results greater than the maximum number of entries allowed by the app (no start param) - Bad request`() {
        withTestApplication({ gakusciModule() }) {
            val bigStartValue = 1 shl 20
            handleRequest(
                HttpMethod.Get,
                "/api/v1/researches/?q=aabcd&max_results=$bigStartValue"
            ).apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }
}