package io.gumichan01.gakusci.domain.utils

import io.gumichan01.gakusci.domain.model.SearchResponse

fun SearchResponse.take(n: Int) = this.copy(entries = entries.take(n))