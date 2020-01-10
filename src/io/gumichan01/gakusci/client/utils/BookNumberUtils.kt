package io.gumichan01.gakusci.client.utils


fun normalizeIsbn(bookNumber: String) = bookNumber.replace(Regex("[- ]"), "")

// Check https://www.loc.gov/marc/lccn-namespace.html to get information about how LCCNs are normalized
fun normalizeLccn(bookNumber: String): String {
    return bookNumber.replace(Regex(" "), "").substringBefore('/').split('-').normalize()
}

private fun List<String>.normalize(): String {
    return when (size) {
        1 -> first()
        2 -> {
            val zeros = "000000"
            val second = last()
            first() + (if (second.length == zeros.length) second else zeros.substring(second.length) + second)
        }
        // Hmmm! This is not a valid normalized LCCN, but it is not the purpose of this method to check that
        else -> this.reduce { acc, s -> acc + s }
    }
}

fun isValidISBN(bookNumber: String): Boolean {
    return isValidISBN10(bookNumber) || isValidISBN13(bookNumber)
}

// Check https://isbn-information.com/the-10-digit-isbn.html
private fun isValidISBN10(bookNumber: String): Boolean {
    val isbn10WithoutSeparatorRegex = "\\d{10}\$"
    val isbn10Regex = "\\d{1,5}([- ])\\d{1,7}\\1\\d{1,6}\\1(\\d|X)\$"
    return bookNumber.matches(Regex("$isbn10Regex|$isbn10WithoutSeparatorRegex")) && isValidCheckDigitISBN10(bookNumber)
}

private fun isValidCheckDigitISBN10(bookNumber: String): Boolean {
    val simplifiedBookNumber = normalizeIsbn(bookNumber)
    return simplifiedBookNumber.length == 10 &&
            simplifiedBookNumber.map { c -> c.toInt() }
                .mapIndexed { i, v -> v * (10 - i) }
                .reduce { acc, sum -> acc + sum } % 11 == 0
}

// Check https://isbn-information.com/the-13-digit-isbn.html
private fun isValidISBN13(bookNumber: String): Boolean {
    val isbn13WithoutSeparatorRegex = "\\d{13}\$"
    val isbn13Regex = "97(?:8|9)([ -])\\d{1,5}\\1\\d{1,7}\\1\\d{1,6}\\1\\d\$"
    return bookNumber.matches(Regex("$isbn13Regex|$isbn13WithoutSeparatorRegex")) && isValidCheckDigitISBN13(bookNumber)
}

private fun isValidCheckDigitISBN13(bookNumber: String): Boolean {
    val simplifiedBookNumber = normalizeIsbn(bookNumber)
    return simplifiedBookNumber.length == 13 &&
            simplifiedBookNumber.map { c -> c.toInt() }
                .mapIndexed { i, v -> v * (if ((i % 2) == 0) 1 else 3) }
                .reduce { acc, sum -> acc + sum } % 10 == 0
}
