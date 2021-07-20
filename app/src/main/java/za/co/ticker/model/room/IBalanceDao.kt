package za.co.ticker.model.room

import androidx.room.Dao
import androidx.room.Query
import za.co.ticker.model.models.Balance

@Dao
interface IBalanceDao : IBaseDao<Balance> {
    @Query("SELECT * FROM balance")
    suspend fun getAll(): List<Balance>
}