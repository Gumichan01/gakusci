package io.gumichan01.gakusci.domain.model

import io.gumichan01.gakusci.domain.model.entry.IResultEntry

data class ServiceResponse(val totalResults: Int, val entries: List<IResultEntry>)