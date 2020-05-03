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
    fun `test static homepage`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
            }
        }
    }

    @Test
    fun `test web application, normal case with research - return OK and HTML content`() {
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
    fun `test web application, normal case with books - return OK and HTML content`() {
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
    fun `test web application, search for a book by ISBN - return OK and HTML content`() {
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
    fun `test web application search, query with negative start value - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/search/?q=lorem&stype=research&start=-10").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `test web application, empty query - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/search/?q=&stype=research").apply {
                with(response) {
                    assertThat(status()).isEqualTo(HttpStatusCode.BadRequest)
                }
            }
        }
    }

    @Test
    fun `test web application, blank query - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/search/?q=     &stype=research").apply {
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

    @Test
    fun `test web application search, query with very big start value - return Bad request`() {
        withTestApplication({ gakusciModule() }) {
            val bigStartValue = 1 shl 20
            handleRequest(HttpMethod.Get, "/search/?q=lorem&stype=research&start=$bigStartValue").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `test webapp search, query that returns more than 2000 results - return OK with good start value of the last page (HTML)`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/search/?q=fruit&stype=research&start=0").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
                assertThat(response.content?.substringAfterLast("start=")?.substringBefore("\"")?.toInt())
                    .isEqualTo(MAX_ENTRIES - 10)
            }
        }
    }

    @Test
    fun `test webapp search, query that returns less than 2000 results - return OK with good start value of the last page (HTML)`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/search/?q=ipsum&stype=research&start=0").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
                assertThat(response.content?.substringAfterLast("start=")?.substringBefore("\"")?.toInt())
                    .isLessThan(MAX_ENTRIES - 10)
            }
        }
    }
}

