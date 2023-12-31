package io.gumichan01.gakusci

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

@FlowPreview
@ExperimentalCoroutinesApi
class RestApiTest {
    @Test
    fun `test REST Web-service API v1 search, normal case - return OK and non empty content`() {
        testApplication {
            val response = client.get("/api/v1/books/?q=gunnm")
            assertThat(response.status).isEqualTo(HttpStatusCode.OK)
            assertThat(response.bodyAsText()).isNotBlank()
        }
    }

    @Test
    fun `test REST Web-service API v1 search, no query - return Bad request`() {
        testApplication {
            val response = client.get("/api/v1/papers/")
            assertThat(response.status).isEqualTo(HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun `test REST Web-service API v1 search, blank query - returns Bad request`() {
        testApplication {
            val response = client.get("/api/v1/researches/?q=     ")
            with(response) {
                assertThat(status).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `webapp, query too short (less than 3 characters) - returns Bad request`() {
        testApplication {
            val response = client.get("/api/v1/researches/?q=a")
            with(response) {
                assertThat(status).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `test REST Web-service API v1 search, query with start but no rows - return Bad request`() {
        testApplication {
            val response = client.get("/api/v1/researches/?q=abcd&start=0")
            assertThat(response.status).isEqualTo(HttpStatusCode.BadRequest)

        }
    }

    @Test
    fun `test REST Web-service API v1 search, query with pagination (start, rows) - return ok`() {
        testApplication {
            val response = client.get("/api/v1/books/?q=manga&start=1&rows=4")
            assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        }
    }

    @Test
    fun `test REST Web-service API v1 search, query with start greater than rows - return Bad request`() {
        testApplication {
            val response = client.get("/api/v1/researches/?q=abcda&rows=10&start=100")
            assertThat(response.status).isEqualTo(HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun `test REST Web-service API v1 search, query with negative start value - return Bad request`() {
        testApplication {
            val response = client.get("/api/v1/researches/?q=abcda&start=-10&rows=16")
            assertThat(response.status).isEqualTo(HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun `test REST Web-service API v1 search, pagination but with negative rows - return Bad request`() {
        testApplication {
            val response = client.get("/api/v1/researches/?q=abcdb&rows=-1&start=0")
            assertThat(response.status).isEqualTo(HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun `test REST Web-service API v1 search, negative rows without pagination - return Bad request`() {
        testApplication {
            val response = client.get("/api/v1/researches/?q=abcdb&rows=-1")
            assertThat(response.status).isEqualTo(HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun `test REST Web-service API v1 search, query with numPerPage greater than rows - return Bad request`() {
        testApplication {
            val response = client.get("/api/v1/researches/?q=babcd&rows=10&start=1&")
            assertThat(response.status).isEqualTo(HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun `test REST API v1, rows greater than the maximum number of entries allowed by the app - Bad request`() {
        testApplication {
            val bigStartValue = 1 shl 20
            val response = client.get("/api/v1/researches/?q=aabcd&start=0&rows=$bigStartValue")
            assertThat(response.status).isEqualTo(HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun `test REST API v1, rows greater than the maximum number of entries allowed by the app (no start param) - Bad request`() {
        testApplication {
            val bigStartValue = 1 shl 20
            val response = client.get("/api/v1/researches/?q=aabcd&rows=$bigStartValue")
            assertThat(response.status).isEqualTo(HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun `Redirect query in REST API - return bad request`() {
        testApplication {
            val response = client.get("/api/v1/researches/?q=!hal")
            assertThat(response.status).isEqualTo(HttpStatusCode.BadRequest)
        }
    }
}
