package za.co.botcoin.model.room

import androidx.room.Dao
import androidx.room.Query
import za.co.botcoin.model.models.Trade

@Dao
interface ITradeDao : IBaseDao<Trade> {
    @Query("SELECT * FROM trade")
    suspend fun getAll(): List<Trade>
}