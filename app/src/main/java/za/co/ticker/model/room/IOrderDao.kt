package za.co.ticker.model.room

import androidx.room.Dao
import androidx.room.Query
import za.co.ticker.model.models.Order

@Dao
interface IOrderDao : IBaseDao<Order> {
    @Query("SELECT * FROM 'order' ORDER BY state DESC")
    suspend fun getAll(): List<Order>
}