package io.gumichan01.gakusci

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.ktor.test.dispatcher.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

@FlowPreview
@ExperimentalCoroutinesApi
class ApplicationTest {
    @Test
    fun `webapp, static homepage - returns OK`() {
        testApplication {
            environment { config = ApplicationConfig("application.conf") }
            val response: HttpResponse = client.get("/")
            assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        }
    }

    @Test
    fun `webapp, normal case with research papers - returns OK and HTML body`() {
        testApplication {
            environment { config = ApplicationConfig("application.conf") }
            val response: HttpResponse = client.get("/search/?q=lorem&stype=papers")
            with(response) {
                assertThat(status).isEqualTo(HttpStatusCode.OK)
                assertThat(bodyAsText()).isNotBlank
                assertThat(bodyAsText()).contains(
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

    @Test
    fun `webapp, normal case with books - returns OK and HTML body`() {
        testApplication {
            environment { config = ApplicationConfig("application.conf") }
            val response: HttpResponse = client.get("/search/?q=lorem&stype=books")
            with(response) {
                assertThat(status).isEqualTo(HttpStatusCode.OK)
                assertThat(bodyAsText()).isNotBlank
                assertThat(bodyAsText()).contains(
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

    @Test
    fun `webapp, search for a book by ISBN - returns OK and HTML body`() {
        testApplication {
            environment { config = ApplicationConfig("application.conf") }
            val response: HttpResponse = client.get("/search/?q=ISBN:9784088766829&stype=books")
            with(response) {
                assertThat(status).isEqualTo(HttpStatusCode.OK)
                assertThat(bodyAsText()).isNotBlank
                assertThat(bodyAsText()).contains(
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

    @Test
    fun `webapp, query with negative start value - returns Bad request`() {
        testApplication {
            environment { config = ApplicationConfig("application.conf") }
            val response: HttpResponse = client.get("/search/?q=lorem&stype=book&start=-10")
            assertThat(response.status).isEqualTo(HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun `webapp, empty query - returns Bad request`() {
        testApplication {
            environment { config = ApplicationConfig("application.conf") }
            val response: HttpResponse = client.get("/search/?q=&stype=books")
            with(response) {
                assertThat(status).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `webapp, blank query - returns Bad request`() {
        testApplication {
            environment { config = ApplicationConfig("application.conf") }
            val response: HttpResponse = client.get("/search/?q=     &stype=books")
            with(response) {
                assertThat(status).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `webapp, query too short (less than 3 characters) - returns Bad request`() {
        testApplication {
            environment { config = ApplicationConfig("application.conf") }
            val response: HttpResponse = client.get("/search/?q=a&stype=books")
            with(response) {
                assertThat(status).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }


    @Test
    fun `webapp, no search type for a query - returns Bad request`() {
        testApplication {
            environment { config = ApplicationConfig("application.conf") }
            val response: HttpResponse = client.get("/search/?q=science")
            with(response) {
                assertThat(status).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }


    @Test
    fun `Query 'fruit' returning more than 1000 results (10 entries per page) - returns HTML body with 'start' value of the last page less than 1990`() {
        testApplication {
            environment { config = ApplicationConfig("application.conf") }
            val response: HttpResponse = client.get("/search/?q=fruit&stype=books")
            assertThat(response.status).isEqualTo(HttpStatusCode.OK)
            assertThat(response.bodyAsText().substringAfterLast("start=").substringBefore("\"").toInt())
                .isLessThanOrEqualTo(1000)
        }
    }


    @Test
    fun `Query 'lorem' that returns less than 1000 results (10 entries per page) - returns HTML body with 'start' value of the last page less than 1990`() {
        testApplication {
            environment { config = ApplicationConfig("application.conf") }
            val response: HttpResponse = client.get("/search/?q=fruit&stype=books")
            assertThat(response.status).isEqualTo(HttpStatusCode.OK)
            assertThat(response.bodyAsText().substringAfterLast("start=").substringBefore("\"").toInt())
                .isLessThanOrEqualTo(1000)
        }
    }

    @Test
    fun `Redirect query with bad bang request - return bad request`() {
        testApplication {
            environment { config = ApplicationConfig("application.conf") }
            val response: HttpResponse = client.get("/search/?q=!invalidbang")
            assertThat(response.status).isEqualTo(HttpStatusCode.BadRequest)
        }
    }

}
