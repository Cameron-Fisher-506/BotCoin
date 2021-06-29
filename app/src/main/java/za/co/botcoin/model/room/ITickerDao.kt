package za.co.botcoin.model.room

import androidx.room.Dao
import androidx.room.Query
import za.co.botcoin.model.models.Ticker

@Dao
interface ITickerDao: IBaseDao<Ticker> {
    @Query("SELECT * FROM ticker ORDER BY timestamp = :timestamp")
    suspend fun getMostRecentByPair(pair: String): Ticker?
}