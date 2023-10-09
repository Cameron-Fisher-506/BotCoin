package za.co.botcoin.utils.services.cacheService

object BaseCacheService {
    private val cache: HashMap<String, Any> = hashMapOf()

    const val ORDER_BOOK = "ORDER_BOOK"

    fun getValue(key: String): Any = cache[key] as Any
    fun setValue(key: String, value: Any) = cache.put(key, value)

    fun clear() = cache.clear()
}