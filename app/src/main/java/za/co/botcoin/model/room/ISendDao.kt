package za.co.botcoin.model.room

import androidx.room.Dao
import androidx.room.Query
import za.co.botcoin.model.models.Send

@Dao
interface ISendDao : IBaseDao<Send> {
    @Query("SELECT * FROM send")
    suspend fun getAll(): List<Send>
}