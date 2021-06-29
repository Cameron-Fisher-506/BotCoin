package za.co.botcoin.model.room

import androidx.room.Dao
import za.co.botcoin.model.models.Luno

@Dao
interface ILunoDao : IBaseDao<Luno> {
}