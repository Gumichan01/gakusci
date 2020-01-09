package io.gumichan01.gakusci.client.utils


fun isValidISBN(bookNumber: String): Boolean {
    val isbn10WithoutSeparatorRegex = "\\d{10}\$"
    val isbn10Regex = "\\d{1,5}([- ])\\d{1,7}\\1\\d{1,6}\\1(\\d|X)\$"
    return bookNumber.matches(Regex("$isbn10Regex|$isbn10WithoutSeparatorRegex"))
}