package io.gumichan01.gakusci.client.openlib

import com.fasterxml.jackson.annotation.JsonProperty

data class OpenLibraryEntry(
    @JsonProperty("title_suggest") val titleSuggest: String,
    @JsonProperty("edition_key") val editionKeys: List<String>?,
    @JsonProperty("cover_i") val coverIndex: Int,
    @JsonProperty("isbn") val isbns: List<String>?,
    @JsonProperty("oclc") val oclcIds: List<String>?,
    @JsonProperty("lccn") val lccnIds: List<String>?,
    @JsonProperty("has_fulltext") val hasFulltext: Boolean,
    @JsonProperty("text") val texts: List<String>,
    @JsonProperty("seed") val seeds: List<String>,
    @JsonProperty("first_sentence") val firstSentences: List<String>?,
    @JsonProperty("id_librarything") val idLibraryThings: List<String>?,
    @JsonProperty("id_goodreads") val goodReadsIds: List<String>?,
    @JsonProperty("id_amazon") val amazonIds: List<String>?,
    @JsonProperty("id_google") val googleIds: List<String>?,
    @JsonProperty("id_alibris_id") val alibrisIds: List<String>?,
    @JsonProperty("id_overdrive") val overdriveIds: List<String>?,
    @JsonProperty("id_paperback_swap") val paperbackIds: List<String>?,
    @JsonProperty("id_depósito_legal") val depositIds: List<String>?,
    @JsonProperty("id_hathi_trust") val hathiIds: List<String>?,
    @JsonProperty("id_project_gutenberg") val gutenbergIds: List<String>?,
    @JsonProperty("id_wikidata") val wikidataIds: List<String>?,
    @JsonProperty("ia") val ias: List<String>?,
    @JsonProperty("ia_collection_s") val iaCollections: String?,
    @JsonProperty("ia_box_id") val iaBoxIds: List<String>?,
    @JsonProperty("ia_loaded_id") val iaLoadedIds: List<String>?,
    @JsonProperty("author_key") val authorKeys: List<String>?,
    @JsonProperty("subject") val subjects: List<String>?,
    @JsonProperty("time") val time: List<String>?,
    @JsonProperty("type") val type: String,
    @JsonProperty("printdisabled_s") val printDisabled: String?,
    @JsonProperty("publish_place") val publishPlace: List<String>?,
    @JsonProperty("ebook_count_i") val ebookCount: Int,
    @JsonProperty("edition_count") val editionCount: Int,
    @JsonProperty("lending_identifier_s") val lendingId: String?,
    @JsonProperty("lending_edition_s") val lendingEdition: String?,
    @JsonProperty("title") val title: String, // Title, I need that
    @JsonProperty("subtitle") val subtitle: String?, // Title, I need that
    @JsonProperty("author_name") val authorNames: List<String>?, // Author to get
    @JsonProperty("key") val key: String, // This is the path to the resource form https://openlibrary.org
    @JsonProperty("first_publish_year") val firstPublishYear: Int, // First Publish Year to get if there are several dates
    @JsonProperty("publish_date") val publishDates: List<String>?, // Publish date to get
    @JsonProperty("publish_year") val publishYears: List<String>?,
    @JsonProperty("contributor") val contributors: List<String>?,
    @JsonProperty("person") val persons: List<String>?,
    @JsonProperty("public_scan_b") val publicScan: String?,
    @JsonProperty("publisher") val publishers: List<String>?,
    @JsonProperty("language") val languages: List<String>?,
    @JsonProperty("last_modified_i") val lastModifiedTimestamp: Long?,
    @JsonProperty("author_alternative_name") val authorAltNames: List<String>?,
    @JsonProperty("cover_edition_key") val coverEditionKey: String?,
    @JsonProperty("place") val places: List<String>?
) {
    fun label(): String {
        val author = authors()
        val date = publishDate()
        return "$title, $author, $date"
    }

    private fun authors(): String {
        return when {
            authorNames == null || authorNames.isEmpty() -> "Unknown author"
            authorNames.size > 1 -> authorNames[0] + " et al."
            else -> authorNames[0]
        }
    }

    private fun publishDate(): String {
        return "(${if (firstPublishYear == 0) "n.d" else firstPublishYear.toString()})"
    }

    fun link(): String {
        return "https://openlibrary.org$key"
    }
}
