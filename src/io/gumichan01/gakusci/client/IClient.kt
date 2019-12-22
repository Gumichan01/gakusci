package io.gumichan01.gakusci.client

import io.gumichan01.gakusci.domain.model.QueryParam

interface IClient<out R> {
    suspend fun retrieveResults(queryParam: QueryParam): R?
}