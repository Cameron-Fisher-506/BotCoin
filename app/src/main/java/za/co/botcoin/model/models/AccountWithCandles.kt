package za.co.botcoin.model.models

import androidx.room.Embedded
import androidx.room.Relation

data class AccountWithCandles(
    @Embedded
    var account: Account = Account(),

    @Relation(parentColumn = "id", entityColumn = "accountId")
    var candles: List<Candle>?,
    var duration: Int = 0,
    var pair: String = ""
) : BaseModel()