package za.co.botcoin.model.models

import androidx.room.Embedded
import androidx.room.Relation

data class AccountWithTickers(
        @Embedded
        val account: Account = Account(),

        @Relation(parentColumn = "id", entityColumn = "lunoId")
        var tickers: List<Ticker>
) : BaseModel()