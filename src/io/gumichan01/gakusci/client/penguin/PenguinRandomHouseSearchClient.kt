package io.gumichan01.gakusci.client.penguin

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.utils.trace
import io.gumichan01.gakusci.domain.model.QueryParam
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.NodeList
import java.io.ByteArrayInputStream
import java.nio.charset.Charset
import javax.xml.parsers.DocumentBuilderFactory

class PenguinRandomHouseSearchClient : IClient<PenguinRandomHouseSearchResponse> {

    private val logger: Logger = LoggerFactory.getLogger(PenguinRandomHouseSearchClient::class.java)
    private val searchUrl = "https://reststop.randomhouse.com/resources/titles?start=0&max=%d&expandLevel=0&search=%s"

    override suspend fun retrieveResults(queryParam: QueryParam): PenguinRandomHouseSearchResponse? {
        val url: String = searchUrl.format(queryParam.rows, queryParam.query)

        return try {
//            InputStreamReader(FileInputStream(File("/home/luxon/prive/gumichan01/gakusci/marx.xml"))).use { it.readText() }
//                .let { xmlText ->
            PenguinRandomHouseSearchResponse(extractIsbns(HttpClient(Apache).use { it.get<String>(url) }))
        } catch (e: Exception) {
            trace(logger, e)
            null
        }
    }

    private fun extractIsbns(xmlText: String): List<String> {
        val input = ByteArrayInputStream(xmlText.toByteArray(Charset.forName("UTF-8")))
        val elementsByTagName: NodeList = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder().parse(input).documentElement
            .apply { this.normalize() }
            .getElementsByTagName("isbn")

        val isbns: MutableList<String?> = mutableListOf()
        for (i in 0..elementsByTagName.length) {
            isbns.add(elementsByTagName.item(i)?.textContent)
        }
        return isbns.filterNotNull()
    }
}