package za.co.botcoin.model.models

import androidx.room.Embedded
import androidx.room.Relation

data class AccountWithBalances(
        @Embedded
        val account: Account = Account(),

        @Relation(parentColumn = "id", entityColumn = "lunoAccountId")
        var balances: List<Balance>
) : BaseModel()