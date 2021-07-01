package za.co.botcoin.model.room

import androidx.room.Dao
import androidx.room.Query
import za.co.botcoin.model.models.StopOrder

@Dao
interface IStopOrderDao : IBaseDao<StopOrder> {
    @Query("SELECT * FROM stoporder")
    suspend fun getAll(): List<StopOrder>
}