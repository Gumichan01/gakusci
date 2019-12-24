package io.gumichan01.gakusci.domain.search.cache

import com.github.benmanes.caffeine.cache.Cache
import io.gumichan01.gakusci.domain.model.ServiceResponse

class SearchCache(cache: Cache<Pair<String, Int>, ServiceResponse>) :
    Cache<Pair<String, Int>, ServiceResponse> by cache {

}
