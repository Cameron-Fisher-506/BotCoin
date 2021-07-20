package za.co.botcoin.model.room

import androidx.room.Dao
import za.co.botcoin.model.models.Account

@Dao
interface IAccountDao : IBaseDao<Account> {
}