package za.co.botcoin.model.room

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface IBaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entities: List<T>): List<Long>

    @Update
    suspend fun update(entity: T)

    @Update
    suspend fun update(entities: List<T>)

    @Delete
    suspend fun delete(entity: T)

    @Delete
    suspend fun delete(entities: List<T>)

    @RawQuery
    suspend fun specialQuery(query: SupportSQLiteQuery): List<T>?
}