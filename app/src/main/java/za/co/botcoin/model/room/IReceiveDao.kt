package za.co.botcoin.model.room

import androidx.room.Dao
import androidx.room.Query
import za.co.botcoin.model.models.Receive

@Dao
interface IReceiveDao : IBaseDao<Receive> {
    @Query("SELECT * FROM receive")
    suspend fun getAll(): List<Receive>
}