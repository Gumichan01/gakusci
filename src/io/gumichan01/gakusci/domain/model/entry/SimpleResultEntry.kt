package io.gumichan01.gakusci.domain.model.entry

data class SimpleResultEntry(val label: String, val url: String): IResultEntry {
    override fun label() = label
    override fun link() = url
}