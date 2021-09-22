package za.co.botcoin.model.room

import androidx.room.Dao
import androidx.room.Query
import za.co.botcoin.model.models.Candle

@Dao
interface ICandleDao : IBaseDao<Candle> {
    @Query("SELECT * FROM candle ORDER BY id DESC LIMIT 24")
    suspend fun getAll(): List<Candle>

    @Query("SELECT * FROM candle ORDER BY id DESC LIMIT 1")
    suspend fun getLast(): Candle
}