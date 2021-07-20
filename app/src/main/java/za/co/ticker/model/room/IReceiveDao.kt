package za.co.ticker.model.room

import androidx.room.Dao
import androidx.room.Query
import za.co.ticker.model.models.Receive

@Dao
interface IReceiveDao : IBaseDao<Receive> {
    @Query("SELECT * FROM receive")
    suspend fun getAll(): List<Receive>
}