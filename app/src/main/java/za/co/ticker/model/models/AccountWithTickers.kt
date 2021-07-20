package za.co.ticker.model.models

import androidx.room.Embedded
import androidx.room.Relation

data class AccountWithTickers(
        @Embedded
        val account: Account = Account(),

        @Relation(parentColumn = "id", entityColumn = "accountId")
        var tickers: List<Ticker>?
) : BaseModel()