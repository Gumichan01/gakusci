package io.gumichan01.gakusci.client.exception

data class RateLimitViolationException(override val message: String) : Exception()