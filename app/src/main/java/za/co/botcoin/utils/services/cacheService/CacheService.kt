package za.co.botcoin.utils.services.cacheService

import za.co.botcoin.model.websocket.dto.OrderBookResponse
import za.co.botcoin.utils.services.cacheService.BaseCacheService.ORDER_BOOK

class CacheService : ICacheService {
    override fun getOrderBookResponse(): OrderBookResponse =
        BaseCacheService.getValue(ORDER_BOOK) as OrderBookResponse

    override fun setOrderBookResponse(orderBookResponse: OrderBookResponse) {
        BaseCacheService.setValue(ORDER_BOOK, orderBookResponse)
    }

    override fun clearCache() = BaseCacheService.clear()
}