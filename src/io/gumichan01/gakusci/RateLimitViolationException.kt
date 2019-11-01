package io.gumichan01.gakusci

data class RateLimitViolationException(override val message: String) : Exception()