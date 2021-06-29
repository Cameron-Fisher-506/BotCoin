package za.co.botcoin.model.room

import android.content.Context
import androidx.lifecycle.liveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import za.co.botcoin.model.models.Luno
import za.co.botcoin.model.models.Order
import za.co.botcoin.model.models.Ticker
import za.co.botcoin.utils.Resource
import java.lang.Exception

@Database(entities = [Ticker::class, Luno::class], version = 1, exportSchema = false)
abstract class BotCoinDatabase : RoomDatabase() {

    abstract fun tickerDao(): ITickerDao
    abstract fun lunoDao(): ILunoDao

    companion object {
        @Volatile
        private var INSTANCE: BotCoinDatabase? = null

        fun getDatabase(context: Context): BotCoinDatabase {
            val tempInstance = INSTANCE

            if(tempInstance != null) {
                return tempInstance
            } else {
                synchronized(this) {
                    val instance = Room.databaseBuilder(context.applicationContext, BotCoinDatabase::class.java, "botcoin").build()
                    INSTANCE = instance
                    return instance
                }
            }
        }

        inline fun <T> getLiveDataResource(crossinline daoCall: suspend () -> T?) = liveData<Resource<T>>(Dispatchers.IO) {
            emit(Resource.loading())

            val value = daoCall.invoke()
            if (value != null) {
                emit(Resource.success(value))
            } else {
                emit(Resource.error("Not cached."))
            }
        }

        suspend inline fun <T> getResource(crossinline daoCall: suspend () -> T?): Resource<T> {
            try {
                val response  = daoCall.invoke()
                if (response != null) {
                    return Resource.success(response)
                }
                return Resource.error("Not found in cache.")
            } catch (e: Exception) {
                return Resource.error(e.message ?: e.toString())
            }
        }
    }
}