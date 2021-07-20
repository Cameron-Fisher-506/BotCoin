package za.co.ticker.model.room

import androidx.room.Dao
import androidx.room.Query
import za.co.ticker.model.models.StopOrder

@Dao
interface IStopOrderDao : IBaseDao<StopOrder> {
    @Query("SELECT * FROM stoporder")
    suspend fun getAll(): List<StopOrder>
}