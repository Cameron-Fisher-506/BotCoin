package za.co.ticker.model.room

import androidx.room.Dao
import androidx.room.Query
import za.co.ticker.model.models.Send

@Dao
interface ISendDao : IBaseDao<Send> {
    @Query("SELECT * FROM send")
    suspend fun getAll(): List<Send>
}