package io.gumichan01.gakusci

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

@KtorExperimentalAPI
@ExperimentalCoroutinesApi
class ApplicationTest {
    @Test
    fun `test static homepage`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
            }
        }
    }

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
    fun `test web application, normal case - return OK and HTML content`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/search/?q=lorem&searchtype=research").apply {
                with(response) {
                    assertThat(status()).isEqualTo(HttpStatusCode.OK)
                    assertThat(content).isNotBlank()
                    assertThat(content).contains(
                        listOf(
                            "<html>",
                            "<div>",
                            "</div>",
                            "<script src=",
                            "</script>",
                            "</html>"
                        )
                    )
                }
            }
        }
    }

    @Test
    fun `test REST Web-service API v1 search, query with start greater than max_results - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/api/v1/researches/?q=a&max_results=10&start=100").apply {
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

    @Test
    fun `test web application, empty query - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/search/?q=").apply {
                with(response) {
                    assertThat(status()).isEqualTo(HttpStatusCode.BadRequest)
                }
            }
        }
    }

    @Test
    fun `test web application, blank query - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/search/?q=     ").apply {
                with(response) {
                    assertThat(status()).isEqualTo(HttpStatusCode.BadRequest)
                }
            }
        }
    }

    @Test
    fun `test web application, query but no search type - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/search/?q=science").apply {
                with(response) {
                    assertThat(status()).isEqualTo(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}

