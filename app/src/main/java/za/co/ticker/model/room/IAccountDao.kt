package za.co.ticker.model.room

import androidx.room.Dao
import za.co.ticker.model.models.Account

@Dao
interface IAccountDao : IBaseDao<Account> {
}