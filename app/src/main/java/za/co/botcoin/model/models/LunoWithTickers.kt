package za.co.botcoin.model.models

import androidx.room.Embedded
import androidx.room.Relation

data class LunoWithTickers(
        @Embedded
        val luno: Luno = Luno(),

        @Relation(parentColumn = "id", entityColumn = "lunoId")
        var tickers: List<Ticker>?
) : BaseModel()