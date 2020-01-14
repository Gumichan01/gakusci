package io.gumichan01.gakusci.client.utils

fun generateBookNumberFromText(text: String): BookNumber? {
    // TODO Detect if the text starts with the type of id follow by ':' as a separator - "<type>:"
    if (text.startsWith("ISBN:")) {
        return if (isValidISBN(text.substringAfter(':'))) BookNumber(BookNumberType.ISBN, normalizeIsbn(text.substringAfter(':'))) else null
    }

    if (text.startsWith("OCLC:")) {
        return if (isValidOCLC(text.substringAfter(':'))) BookNumber(BookNumberType.OCLC, text.substringAfter(':')) else null
    }

    if (text.startsWith("LCCN:")) {
        return if (isValidLCCN(text.substringAfter(':'))) BookNumber(BookNumberType.LCCN, normalizeLccn(text.substringAfter(':'))) else null
    }

    return when {
        isValidISBN(text) -> BookNumber(BookNumberType.ISBN, normalizeIsbn(text))
        isValidOCLC(text) -> BookNumber(BookNumberType.OCLC, text)
        isValidLCCN(text) -> BookNumber(BookNumberType.LCCN, normalizeLccn(text))
        else -> null
    }
}


fun normalizeIsbn(bookNumber: String): String = bookNumber.replace(Regex("[- ]"), "")

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
    val simplifiedIsbn13: String = normalizeIsbn(bookNumber)
    return simplifiedIsbn13.length == 10 &&
            simplifiedIsbn13.map { c -> c.toInt() }
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
    val simplifiedIsbn13: String = normalizeIsbn(bookNumber)
    return simplifiedIsbn13.length == 13 &&
            simplifiedIsbn13.map { c -> c.toInt() }
                .mapIndexed { i, v -> v * (if ((i % 2) == 0) 1 else 3) }
                .reduce { acc, sum -> acc + sum } % 10 == 0
}

// https://www.loc.gov/marc/lccn-namespace.html
fun isValidLCCN(bookNumber: String): Boolean {
    val normalizedLccn: String = normalizeLccn(bookNumber)
    return normalizedLccn.takeLast(8).matches(Regex("[0-9]{8}")) &&
            when (normalizedLccn.length) {
                8 -> true
                9 -> normalizedLccn.first().isLetter()
                10 -> normalizedLccn.take(2).matches(Regex("[0-9]{2}|[a-zA-Z]{2}"))
                11 -> normalizedLccn.take(3).matches(Regex("[a-zA-Z]([0-9]{2}|[a-zA-Z]{2})"))
                12 -> normalizedLccn.take(4).matches(Regex("[a-zA-Z]{2}[0-9]{2}"))
                else -> false
            }
}

fun isValidOCLC(bookNumber: String): Boolean {
    return bookNumber.matches(Regex("[0-9]+"))
}