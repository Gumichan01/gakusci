package io.gumichan01.gakusci.client.hal

import com.fasterxml.jackson.annotation.JsonProperty


data class HalResponse(@JsonProperty("response") val body: HalResponseBody)