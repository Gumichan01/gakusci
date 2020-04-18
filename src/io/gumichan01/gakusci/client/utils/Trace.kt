package io.gumichan01.gakusci.client.utils

import org.slf4j.Logger

fun trace(logger: Logger, e: Exception) {
    logger.trace(e.message)
    if (logger.isTraceEnabled) {
        e.printStackTrace()
    }
}