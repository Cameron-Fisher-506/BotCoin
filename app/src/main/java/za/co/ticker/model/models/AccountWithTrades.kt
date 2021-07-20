package za.co.ticker.model.models

import androidx.room.Embedded
import androidx.room.Relation

data class AccountWithTrades(
        @Embedded
        val account: Account = Account(),

        @Relation(parentColumn = "id", entityColumn = "accountId")
        var trades: List<Trade>?
) : BaseModel()