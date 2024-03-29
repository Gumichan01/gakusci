package io.gumichan01.gakusci.client.utils

enum class BookNumberType(val value: String) {
    ISBN("ISBN"), LCCN("LCCN"), OCLC("OCLC")
}

data class BookNumber(val type: BookNumberType, val value: String)

fun generateBookNumberFromText(text: String): BookNumber? {
    val rawBookNumber: String = text.substringAfter(':')
    return when {
        text.startsWith("ISBN:") -> {
            if (isValidISBN(rawBookNumber)) BookNumber(BookNumberType.ISBN, normalizeIsbn(rawBookNumber)) else null
        }
        text.startsWith("OCLC:") -> {
            if (isValidOCLC(rawBookNumber)) BookNumber(BookNumberType.OCLC, rawBookNumber) else null
        }
        text.startsWith("LCCN:") -> {
            if (isValidLCCN(rawBookNumber)) BookNumber(BookNumberType.LCCN, normalizeLccn(rawBookNumber)) else null
        }
        isValidISBN(text) -> BookNumber(BookNumberType.ISBN, normalizeIsbn(text))
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
fun isValidISBN10(bookNumber: String): Boolean {
    val isbn10WithoutSeparatorRegex = "\\d{10}\$"
    val isbn10Regex = "\\d{1,5}([- ])\\d{1,7}\\1\\d{1,6}\\1(\\d|X)\$"
    return bookNumber.matches(Regex("$isbn10Regex|$isbn10WithoutSeparatorRegex")) && isValidCheckDigitISBN10(bookNumber)
}

private fun isValidCheckDigitISBN10(bookNumber: String): Boolean {
    val simplifiedIsbn10: String = normalizeIsbn(bookNumber)
    return simplifiedIsbn10.length == 10 &&
            simplifiedIsbn10.map { c -> c.digitToInt() }
                .mapIndexed { i, v -> v * (10 - i) }
                .reduce { acc, sum -> acc + sum } % 11 == 0
}

// Check https://isbn-information.com/the-13-digit-isbn.html
fun isValidISBN13(bookNumber: String): Boolean {
    val isbn13WithoutSeparatorRegex = "\\d{13}\$"
    val isbn13Regex = "97(?:8|9)([ -])\\d{1,5}\\1\\d{1,7}\\1\\d{1,6}\\1\\d\$"
    return bookNumber.matches(Regex("$isbn13Regex|$isbn13WithoutSeparatorRegex")) && isValidCheckDigitISBN13(bookNumber)
}

private fun isValidCheckDigitISBN13(bookNumber: String): Boolean {
    val simplifiedIsbn13: String = normalizeIsbn(bookNumber)
    return simplifiedIsbn13.length == 13 &&
            simplifiedIsbn13.map { c -> c.digitToInt() }
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

