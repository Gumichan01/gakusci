package io.gumichan01.gakusci.domain.model

sealed class DataSource
object Hal : DataSource()
object Arxiv : DataSource()