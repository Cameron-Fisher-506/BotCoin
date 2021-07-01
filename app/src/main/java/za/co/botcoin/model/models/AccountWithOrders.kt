package za.co.botcoin.model.models

import androidx.room.Embedded
import androidx.room.Relation

data class AccountWithOrders(
        @Embedded
        val account: Account = Account(),

        @Relation(parentColumn = "id", entityColumn = "accountId")
        var orders: List<Order>?
) : BaseModel()