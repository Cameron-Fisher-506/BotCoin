package za.co.botcoin.view.home

import androidx.lifecycle.ViewModel
import za.co.botcoin.model.websocket.dto.OrderBookResponse
import za.co.botcoin.utils.services.cacheService.ICacheService

class HomeViewModel(
    private val cacheService: ICacheService
): ViewModel() {
    fun cacheOrderBookResponse(orderBookResponse: OrderBookResponse) = cacheService.setOrderBookResponse(orderBookResponse)
}