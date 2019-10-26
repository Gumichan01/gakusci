package io.gumichan01.gakusci.client.hal

data class HalResponseBody(val numFound: Int, val start: Int, val docs: List<HalResultEntry>?)