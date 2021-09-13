package za.co.botcoin.model.room

import androidx.room.Dao
import androidx.room.Query
import za.co.botcoin.model.models.Candle

@Dao
interface ICandleDao : IBaseDao<Candle> {
    @Query("SELECT * FROM candle")
    suspend fun getAll(): List<Candle>
}