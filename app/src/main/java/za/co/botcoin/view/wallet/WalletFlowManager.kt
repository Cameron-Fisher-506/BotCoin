package za.co.botcoin.view.wallet

import za.co.botcoin.model.models.Order
import javax.inject.Inject

class WalletFlowManager @Inject constructor() {
    var ordersResponse: List<Order> = emptyList()
}