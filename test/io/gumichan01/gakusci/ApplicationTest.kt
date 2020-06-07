package io.gumichan01.gakusci

import io.gumichan01.gakusci.controller.utils.MAX_ENTRIES
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
class ApplicationTest {
    @Test
    fun `webapp, static homepage - returns OK`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
            }
        }
    }

    @Test
    fun `webapp, normal case with research - returns OK and HTML content`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/search/?q=lorem&stype=research").apply {
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
    fun `webapp, normal case with books - returns OK and HTML content`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/search/?q=lorem&stype=books").apply {
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
    fun `webapp, search for a book by ISBN - returns OK and HTML content`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/search/?q=ISBN:9784088766829&stype=books").apply {
                with(response) {
                    assertThat(status()).isEqualTo(HttpStatusCode.OK)
                    assertThat(content).isNotBlank()
                    assertThat(content).contains(
                        listOf(
                            "<html>",
                            "<div>",
                            "ISBN",
                            "9784088766829",
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
    fun `webapp, query with negative start value - returns Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/search/?q=lorem&stype=research&start=-10").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `webapp, empty query - returns Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/search/?q=&stype=research").apply {
                with(response) {
                    assertThat(status()).isEqualTo(HttpStatusCode.BadRequest)
                }
            }
        }
    }

    @Test
    fun `webapp, blank query - returns Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/search/?q=     &stype=research").apply {
                with(response) {
                    assertThat(status()).isEqualTo(HttpStatusCode.BadRequest)
                }
            }
        }
    }

    @Test
    fun `webapp, query too short (less than 3 characters) - returns Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/search/?q=a&stype=research").apply {
                with(response) {
                    assertThat(status()).isEqualTo(HttpStatusCode.BadRequest)
                }
            }
        }
    }

    @Test
    fun `webapp, no search type for a query - returns Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/search/?q=science").apply {
                with(response) {
                    assertThat(status()).isEqualTo(HttpStatusCode.BadRequest)
                }
            }
        }
    }

    @Test
    fun `test web application search, query with very big start value - returns Bad request`() {
        withTestApplication({ gakusciModule() }) {
            val bigStartValue = 1 shl 20
            handleRequest(HttpMethod.Get, "/search/?q=lorem&stype=research&start=$bigStartValue").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `Query 'fruit' returning more than 2000 results (10 entries per page) - returns HTML content with 'start' value of the last page less than 1990`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/search/?q=fruit&stype=research").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
                assertThat(response.content?.substringAfterLast("start=")?.substringBefore("\"")?.toInt())
                    .isEqualTo(MAX_ENTRIES - 10)
            }
        }
    }

    @Test
    fun `Query 'lorem' that returns less than 2000 results (10 entries per page) - returns HTML content with 'start' value of the last page less than 1990`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/search/?q=ipsum&stype=research").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
                assertThat(response.content?.substringAfterLast("start=")?.substringBefore("\"")?.toInt())
                    .isLessThan(MAX_ENTRIES - 10)
            }
        }
    }

//    @Test
    fun `Redirect query - return redirect`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/search/?q=!hal").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.Found)
            }
        }
    }
}

