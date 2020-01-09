package io.gumichan01.gakusci.client.utils


fun isValidISBN(bookNumber: String): Boolean {
    return isValidISBN10(bookNumber)
}

// Check https://isbn-information.com/the-10-digit-isbn.html
private fun isValidISBN10(bookNumber: String): Boolean {
    val isbn10WithoutSeparatorRegex = "\\d{10}\$"
    val isbn10Regex = "\\d{1,5}([- ])\\d{1,7}\\1\\d{1,6}\\1(\\d|X)\$"
    return bookNumber.matches(Regex("$isbn10Regex|$isbn10WithoutSeparatorRegex")) && isValidCheckDigitISBN10(bookNumber)
}

private fun isValidCheckDigitISBN10(bookNumber: String): Boolean {
    val simplifiedBookNumber = bookNumber.replace(Regex("[- ]"), "")
    return simplifiedBookNumber.length == 10 &&
            simplifiedBookNumber.map { c -> c.toInt() }.mapIndexed { i, v -> v * (10 - i) }.reduce { acc, sum -> acc + sum } % 11 == 0
}
