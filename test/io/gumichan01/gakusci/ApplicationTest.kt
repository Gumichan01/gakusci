package io.gumichan01.gakusci

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

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
    fun `test REST Web-service API v1 search - return OK`() {
        withTestApplication({ gakusciModule() }) {
            handleRequest(HttpMethod.Get, "/api/v1/researches/?q=lorem").apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
            }
        }
    }
}

