package io.gumichan01.gakusci.domain.model

data class ServiceResponse(val totalResults: Int, val start: Int, val entries: List<ResultEntry>)