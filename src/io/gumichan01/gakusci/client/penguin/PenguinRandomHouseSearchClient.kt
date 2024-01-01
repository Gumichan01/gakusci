package io.gumichan01.gakusci.client.penguin

import io.gumichan01.gakusci.client.IClient
import io.gumichan01.gakusci.client.utils.NUM_ENTRIES_PER_SERVICE
import io.gumichan01.gakusci.client.utils.trace
import io.gumichan01.gakusci.domain.model.SimpleQuery
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get
import io.ktor.client.statement.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.ByteArrayInputStream
import java.net.URLEncoder
import java.nio.charset.Charset
import javax.xml.parsers.DocumentBuilderFactory

class PenguinRandomHouseSearchClient : IClient<PenguinRandomHouseSearchResponse> {

    private val logger: Logger = LoggerFactory.getLogger(PenguinRandomHouseSearchClient::class.java)
    private val searchUrl = "https://reststop.randomhouse.com/resources/titles?start=0&max=%d&expandLevel=0&search=%s"

    override suspend fun retrieveResults(query: SimpleQuery): PenguinRandomHouseSearchResponse? {
        val url: String = searchUrl.format(NUM_ENTRIES_PER_SERVICE, URLEncoder.encode(query.query, Charsets.UTF_8))
        return try {
            val xmlText: String = HttpClient(Apache).get(url).bodyAsText()
            PenguinRandomHouseSearchResponse(extractIsbnsFromXml(xmlText).take(NUM_ENTRIES_PER_SERVICE))
        } catch (e: Exception) {
            trace(logger, e)
            null
        }
    }

    private fun extractIsbnsFromXml(xmlText: String): List<Pair<String, Set<String>>> {
        val nodes: NodeList = extractNodesFromXmlText(xmlText)
        return retrieveEntries(nodes)
    }

    private fun extractNodesFromXmlText(xmlText: String): NodeList {
        val input = ByteArrayInputStream(xmlText.toByteArray(Charset.forName("UTF-8")))
        return DocumentBuilderFactory.newInstance()
            .newDocumentBuilder().parse(input).documentElement
            .apply { this.normalize() }
            .getElementsByTagName("title")
    }

    /*
        Takes a list of *title* nodes containing an URI and a set of related ISBN and returns a list of pairs (URI, ISBNs).
        Each title node has this following structure (irrelevant children not showed):

        ```
         <title uri="some-uri-value">
            ...
            <relatedisbns>
                <isbn>some-isbn-value</isbn>
            </relatedisbns>
            ...
        </title>
        ```
    */
    private fun retrieveEntries(nodes: NodeList): List<Pair<String, Set<String>>> {
        return nodes.toList().map { titleNode ->    // <title uri ="..."> Node in the XML document
            Pair(
                titleNode.attributes!!.getNamedItem("uri")!!.nodeValue,
                titleNode.childNodes.toList()
                    .first { childNode -> childNode.nodeName == "relatedisbns" }   // <relatedisbn> Node in the XML document
                    .childNodes.toList().asSequence()
                    .filter { child -> child.nodeName == "isbn" }
                    .map { n -> n.firstChild.nodeValue }.toSet()
            )
        }
    }

    private fun NodeList.toList(): List<Node> {
        val nodes: MutableList<Node> = mutableListOf()
        for (i in 0..length) {
            this.item(i)?.let { nodes.add(it) }
        }
        return nodes
    }
}
