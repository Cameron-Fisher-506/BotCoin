package za.co.botcoin.utils.services.cacheService

import za.co.botcoin.model.websocket.dto.OrderBookResponse

class CacheService : ICacheService {
    private val cache: HashMap<String, Any> = hashMapOf()

    companion object {
        const val ORDER_BOOK = "ORDER_BOOK"
    }

    override fun getOrderBookResponse(): OrderBookResponse = cache[ORDER_BOOK] as OrderBookResponse
    override fun setOrderBookResponse(orderBookResponse: OrderBookResponse) {
        cache[ORDER_BOOK] = orderBookResponse
    }

    fun clearCache() = cache.clear()
}