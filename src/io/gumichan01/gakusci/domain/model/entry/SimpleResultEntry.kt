package io.gumichan01.gakusci.domain.model.entry

data class SimpleResultEntry(val label: String, val url: String) : IResultEntry {
    override fun label(): String = label
    override fun link(): String = url
}