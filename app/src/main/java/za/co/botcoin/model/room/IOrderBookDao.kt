package za.co.botcoin.model.room

import androidx.room.Dao
import androidx.room.Query
import za.co.botcoin.model.websocket.dto.Create

@Dao
interface IOrderBookDao : IBaseDao<Create> {
    @Query("SELECT * FROM 'create'")
    suspend fun getAll(): List<Create>

    @Query("SELECT * FROM 'create' WHERE 'orderId'=:id")
    suspend fun getById(id: String): Create
}