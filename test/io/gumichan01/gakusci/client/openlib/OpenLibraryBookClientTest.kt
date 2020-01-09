package io.gumichan01.gakusci.client.openlib

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.domain.model.QueryParam
import io.gumichan01.gakusci.domain.utils.SearchType
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import java.lang.reflect.Method
import kotlin.test.Test

internal class OpenLibraryBookClientTest {

//    @Test
    fun `Call the Open Library web-service - get book by ISBN`() {
        val client: IClient<String> = OpenLibraryBookClient()
        val response: String? = runBlocking { client.retrieveResults(QueryParam("ISBN:1421500574", SearchType.BOOKS)) }
        assertThat(response).isNotNull()
    }

//    @Test // Note: formatBookNumber()  is private, so you need to make it public in order to test is
//    fun `format valid ISBN - get formatted text`() {
//        assertThat(OpenLibraryBookClient().formatBookNumber("978-1-4215-0057-7")).isEqualTo("ISBN:9781421500577")
//    }
//
//    @Test
//    fun `format invalid ISBN - get empty text`() {
//        assertThat(OpenLibraryBookClient().formatBookNumber("978-1-4215-0057-0")).isBlank()
//    }
}