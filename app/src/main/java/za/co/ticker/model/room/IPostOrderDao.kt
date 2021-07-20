package za.co.ticker.model.room

import androidx.room.Dao
import androidx.room.Query
import za.co.ticker.model.models.PostOrder

@Dao
interface IPostOrderDao : IBaseDao<PostOrder> {
    @Query("SELECT * FROM postorder")
    suspend fun getAll(): List<PostOrder>
}