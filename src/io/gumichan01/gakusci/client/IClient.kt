package io.gumichan01.gakusci.client

import io.gumichan01.gakusci.domain.model.SimpleQuery

interface IClient<out R> {
    suspend fun retrieveResults(query: SimpleQuery): R?
}