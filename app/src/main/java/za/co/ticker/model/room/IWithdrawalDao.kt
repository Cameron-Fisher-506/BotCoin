package za.co.ticker.model.room

import androidx.room.Dao
import androidx.room.Query
import za.co.ticker.model.models.Withdrawal

@Dao
interface IWithdrawalDao : IBaseDao<Withdrawal> {
    @Query("SELECT * FROM withdrawal")
    suspend fun getAll(): List<Withdrawal>
}