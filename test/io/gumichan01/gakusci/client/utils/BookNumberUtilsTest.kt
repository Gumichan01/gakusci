package io.gumichan01.gakusci.client.utils

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

internal class BookNumberUtilsTest {

    @Test
    fun `check valid ISBN-10 - no dash, no space`() {
        assertThat(isValidISBN("1421500574")).isTrue()
    }

    @Test
    fun `check valid ISBN-10 - with dashes`() {
        assertThat(isValidISBN("2-1234-5680-2")).isTrue()
    }

    @Test
    fun `check valid ISBN-10 - with spaces`() {
        assertThat(isValidISBN("2 1234 5680 2")).isTrue()
    }

    @Test
    fun `check invalid ISBN-10 - mixed dashes and space`() {
        assertThat(isValidISBN("2 1234-5680 2")).isFalse()
    }

    @Test
    fun `check invalid ISBN-10 - short number`() {
        assertThat(isValidISBN("123")).isFalse()
    }

    @Test
    fun `check invalid ISBN - too long number`() {
        assertThat(isValidISBN("12345-1234567-123456-01")).isFalse()
    }

    @Test
    fun `check invalid ISBN-10 - it contains non-digit, non-dash and non-space characters`() {
        assertThat(isValidISBN("2-1234-K580-2")).isFalse()
    }

    @Test
    fun `check valid ISBN-10 - valid checksum`() {
        assertThat(isValidISBN("0-19-852663-6")).isTrue()
    }

    @Test
    fun `check invalid ISBN-10 - invalid checksum`() {
        assertThat(isValidISBN("0-19-852663-0")).isFalse()
    }

    @Test
    fun `check valid ISBN-13 - no dash, no space`() {
        assertThat(isValidISBN("9781421500577")).isTrue()
    }

    @Test
    fun `check valid ISBN-13 - with dashes`() {
        assertThat(isValidISBN("978-1-4215-0057-7")).isTrue()
    }

    @Test
    fun `check valid ISBN-13 - with spaces`() {
        assertThat(isValidISBN("978 1 4215 0057 7")).isTrue()
    }

    @Test
    fun `check invalid ISBN-13 - mixed dashes and space`() {
        assertThat(isValidISBN("978 1-4215-0057 7")).isFalse()
    }

    @Test
    fun `check invalid ISBN-13 - it contains non-digit, non-dash and non-space characters`() {
        assertThat(isValidISBN("978-1-4215-K057-7")).isFalse()
    }

    @Test
    fun `check valid ISBN-13 - valid checksum`() {
        assertThat(isValidISBN("978-1-4215-0057-7")).isTrue()
    }

    @Test
    fun `check invalid ISBN-13 - invalid checksum`() {
        assertThat(isValidISBN("978-1-4215-0057-0")).isFalse()
    }

    @Test
    fun `normalize LCCN - already normalized`() {
        assertThat(normalizeLccn("n78890351")).isEqualTo("n78890351")
    }

    @Test
    fun `normalize LCCN containing spaces`() {
        assertThat(normalizeLccn("n7 8890 351")).isEqualTo("n78890351")
    }

    @Test
    fun `normalize LCCN containing at least a forward slash`() {
        assertThat(normalizeLccn("n78890351/acc/r75")).isEqualTo("n78890351")
    }

    @Test
    fun `normalize LCCN containing one hyphen - nominal case`() {
        assertThat(normalizeLccn("2001-000002")).isEqualTo("2001000002")
    }

    @Test
    fun `normalize LCCN containing one hyphen - substring following (to the right of) the (removed) hyphen`() {
        assertThat(normalizeLccn("85-2")).isEqualTo("85000002")
    }

    @Test
    fun `normalize LCCN - empty string`() {
        assertThat(normalizeLccn("")).isEqualTo("")
    }

    @Test
    fun `check valid LCCN - 8 characters`() {
        assertThat(isValidLCCN("62019420")).isTrue()
    }

    @Test
    fun `check invalid LCCN - 8 characters, bad character`() {
        assertThat(isValidLCCN("62o19420")).isFalse()
    }

    @Test
    fun `check valid LCCN - 9 characters`() {
        assertThat(isValidLCCN("n78890351")).isTrue()
    }

    @Test
    fun `check invalid LCCN - 9 characters but does not start with a letter`() {
        assertThat(isValidLCCN("278890351")).isFalse()
    }

    @Test
    fun `check valid LCCN - 10 characters starting with two letters`() {
        assertThat(isValidLCCN("nd78890351")).isTrue()
    }

    @Test
    fun `check valid LCCN - 10 characters starting with two digits`() {
        assertThat(isValidLCCN("4278890351")).isTrue()
    }

    @Test
    fun `check invalid LCCN - 10 characters but does not start with either two digits or two letters`() {
        assertThat(isValidLCCN("4d78890351")).isFalse()
    }

    @Test
    fun `check valid LCCN - 11 characters starting with a letter followed by two letters`() {
        assertThat(isValidLCCN("and78890351")).isTrue()
    }

    @Test
    fun `check valid LCCN - 11 characters starting with a letter followed by two digits`() {
        assertThat(isValidLCCN("a4278890351")).isTrue()
    }

    @Test
    fun `check invalid LCCN - 11 characters but the first character is not alphabetic`() {
        assertThat(isValidLCCN("2nd78890351")).isFalse()
    }

    @Test
    fun `check invalid LCCN - 11 characters, the first character is alphabetic but is not followed by either two digits or two letters`() {
        assertThat(isValidLCCN("24d78890351")).isFalse()
    }

    @Test
    fun `check valid LCCN - 12 characters, the first two characters must be alphabetic and the remaining characters digits`() {
        assertThat(isValidLCCN("ba4278890351")).isTrue()
    }

    @Test
    fun `check invalid LCCN - 12 characters, the first two characters are not alphabetic`() {
        assertThat(isValidLCCN("004278890351")).isFalse()
    }

    @Test
    fun `check invalid LCCN - 12 characters, the first two characters are alphabetic but the remaining characters are not digits (1)`() {
        assertThat(isValidLCCN("bazz78890351")).isFalse()
    }

    @Test
    fun `check invalid LCCN - 12 characters, the first two characters are alphabetic but the remaining characters are not digits (2)`() {
        assertThat(isValidLCCN("ba4278890Z51")).isFalse()
    }

    @Test
    fun `check valid OCLC number`() {
        assertThat(isValidOCLC("18936737")).isTrue()
    }

    @Test
    fun `check invalid OCLC number - alphabetic characters`() {
        assertThat(isValidOCLC("1b9e6737")).isFalse()
    }

    @Test
    fun `check invalid OCLC number - special characters`() {
        assertThat(isValidOCLC("1b93$7€7")).isFalse()
    }

    @Test
    fun `generate book number from text - ISBN-10 text OK`() {
        val generateBookNumberFromText = generateBookNumberFromText("1421500574")
        assertThat(generateBookNumberFromText?.type).isEqualTo(BookNumberType.ISBN)
        assertThat(generateBookNumberFromText?.value).isEqualTo("1421500574")
    }

    @Test
    fun `generate book number from text - ISBN-13 text OK`() {
        val generateBookNumberFromText = generateBookNumberFromText("978-1-4215-0057-7")
        assertThat(generateBookNumberFromText?.type).isEqualTo(BookNumberType.ISBN)
        assertThat(generateBookNumberFromText?.value).isEqualTo("9781421500577")
    }

    @Test
    fun `generate book number from text - OCLC text OK`() {
        val generateBookNumberFromText = generateBookNumberFromText("18936737")
        assertThat(generateBookNumberFromText?.type).isEqualTo(BookNumberType.OCLC)
        assertThat(generateBookNumberFromText?.value).isEqualTo("18936737")
    }

    @Test
    fun `generate book number from text - LCCN text OK`() {
        val generateBookNumberFromText = generateBookNumberFromText("ba4278890351")
        assertThat(generateBookNumberFromText?.type).isEqualTo(BookNumberType.LCCN)
        assertThat(generateBookNumberFromText?.value).isEqualTo("ba4278890351")
    }

    @Test
    fun `generate book number from text - unrecognized book number`() {
        val generateBookNumberFromText = generateBookNumberFromText("427$$9oe51")
        assertThat(generateBookNumberFromText?.type).isNull()
    }

    @Test
    fun `generate book number from text - specify book number as ISBN`() {
        val generateBookNumberFromText = generateBookNumberFromText("ISBN:978-1-4215-0057-7")
        assertThat(generateBookNumberFromText?.type).isEqualTo(BookNumberType.ISBN)
        assertThat(generateBookNumberFromText?.value).isEqualTo("9781421500577")
    }

    @Test
    fun `generate book number from text - specify book number as OCLC`() {
        val generateBookNumberFromText = generateBookNumberFromText("OCLC:18936737")
        assertThat(generateBookNumberFromText?.type).isEqualTo(BookNumberType.OCLC)
        assertThat(generateBookNumberFromText?.value).isEqualTo("18936737")
    }

    @Test
    fun `generate book number from text - specify book number as LCCN`() {
        val generateBookNumberFromText = generateBookNumberFromText("LCCN:ba4278890351")
        assertThat(generateBookNumberFromText?.type).isEqualTo(BookNumberType.LCCN)
        assertThat(generateBookNumberFromText?.value).isEqualTo("ba4278890351")
    }
}