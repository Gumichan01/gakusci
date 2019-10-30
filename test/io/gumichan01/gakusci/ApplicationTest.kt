package io.gumichan01.gakusci

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

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
            handleRequest(HttpMethod.Get, "/researches/?q=lorem").apply {
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
}

