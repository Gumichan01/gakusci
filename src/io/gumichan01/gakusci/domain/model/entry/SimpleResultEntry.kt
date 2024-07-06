package io.gumichan01.gakusci.domain.model.entry

data class SimpleResultEntry(val label: String, val url: String) : IResultEntry {
    override fun label(): String = label
    override fun link(): String = url
    override fun cover(): String = throw NotImplementedError("This entry has no cover() implementation")
}