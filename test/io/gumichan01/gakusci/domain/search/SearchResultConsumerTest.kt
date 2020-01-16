package io.gumichan01.gakusci.domain.search

import io.gumichan01.gakusci.domain.model.entry.SimpleResultEntry
import io.gumichan01.gakusci.domain.model.ServiceResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

@FlowPreview
@ExperimentalCoroutinesApi
internal class SearchResultConsumerTest {

    @Test
    fun `consume no result`() {
        val channel = Channel<ServiceResponse>(0).run { close(); this }
        val response: ServiceResponse = runBlocking { SearchResultConsumer().consume(channel) }
        assertThat(response.totalResults).isEqualTo(0)
        assertThat(response.entries).isEmpty()
    }

    @Test
    fun `consume 1 result entry`() {
        val channel = Channel<ServiceResponse>(1).run {
            runBlocking {
                send(ServiceResponse(1, listOf(
                    SimpleResultEntry(
                        "hello",
                        "http://www.example.com"
                    )
                )))
            }
            close()
            this
        }
        val response: ServiceResponse = runBlocking { SearchResultConsumer().consume(channel) }
        assertThat(response.totalResults).isEqualTo(1)
        assertThat(response.entries).isNotEmpty
        assertThat(response.entries).contains(
            SimpleResultEntry(
                "hello",
                "http://www.example.com"
            )
        )
    }

    @Test
    fun `consume 2 result entries from same source`() {
        val channel = Channel<ServiceResponse>(1).run {
            runBlocking {
                send(
                    ServiceResponse(
                        2,
                        listOf(
                            SimpleResultEntry(
                                "hello",
                                "http://www.example.com/1"
                            ),
                            SimpleResultEntry(
                                "world",
                                "http://www.example.com/2"
                            )
                        )
                    )
                )
            }
            close()
            this
        }
        val response: ServiceResponse = runBlocking { SearchResultConsumer().consume(channel) }
        assertThat(response.totalResults).isEqualTo(2)
        assertThat(response.entries).isNotEmpty
        assertThat(response.entries).contains(
            SimpleResultEntry(
                "hello",
                "http://www.example.com/1"
            ),
            SimpleResultEntry(
                "world",
                "http://www.example.com/2"
            )
        )
    }

    @Test
    fun `consume several result entries from different sources`() {
        val channel = Channel<ServiceResponse>(4).run {
            runBlocking {
                send(
                    ServiceResponse(
                        2,
                        listOf(
                            SimpleResultEntry(
                                "hello",
                                "http://www.example.com/1"
                            ),
                            SimpleResultEntry(
                                "world",
                                "http://www.example.com/2"
                            )
                        )
                    )
                )
                send(
                    ServiceResponse(
                        1,
                        listOf(
                            SimpleResultEntry(
                                "foo",
                                "http://www.bar.com"
                            )
                        )
                    )
                )
            }
            close()
            this
        }
        val response: ServiceResponse = runBlocking { SearchResultConsumer().consume(channel) }
        assertThat(response.totalResults).isEqualTo(3)
        assertThat(response.entries).isNotEmpty
        assertThat(response.entries).isEqualTo(
            listOf(
                SimpleResultEntry(
                    "hello",
                    "http://www.example.com/1"
                ),
                SimpleResultEntry(
                    "foo",
                    "http://www.bar.com"
                ),
                SimpleResultEntry(
                    "world",
                    "http://www.example.com/2"
                )
            )
        )
    }
}