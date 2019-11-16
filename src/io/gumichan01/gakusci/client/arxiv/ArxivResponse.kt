package io.gumichan01.gakusci.client.arxiv

data class ArxivResponse(val numFound: Int, val start: Int, val docs: List<ArxivResultEntry>)