package za.co.botcoin.model.room

import androidx.room.Dao
import androidx.room.Query
import za.co.botcoin.model.models.Order

@Dao
interface IOrderDao : IBaseDao<Order> {
    @Query("SELECT * FROM 'order' ORDER BY id DESC")
    suspend fun getAll(): List<Order>
}