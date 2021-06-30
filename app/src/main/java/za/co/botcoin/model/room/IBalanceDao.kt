package za.co.botcoin.model.room

import androidx.room.Dao
import androidx.room.Query
import za.co.botcoin.model.models.Balance

@Dao
interface IBalanceDao : IBaseDao<Balance> {
    @Query("SELECT * FROM balance")
    suspend fun getAll(): List<Balance>
}