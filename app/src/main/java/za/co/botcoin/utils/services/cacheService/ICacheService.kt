package za.co.botcoin.utils.services.cacheService

import za.co.botcoin.model.websocket.dto.OrderBookResponse

interface ICacheService {
    fun getOrderBookResponse(): OrderBookResponse
    fun setOrderBookResponse(orderBookResponse: OrderBookResponse)
}