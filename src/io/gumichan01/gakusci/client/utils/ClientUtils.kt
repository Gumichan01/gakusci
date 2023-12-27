package io.gumichan01.gakusci.client.utils

fun calculatePageToSearchFor(index: Int, nbEntriesPerPage: Int): Int {
    return if (index < nbEntriesPerPage) 1 else index / nbEntriesPerPage + 1
}