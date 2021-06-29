package za.co.botcoin.model.repository

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import za.co.botcoin.model.models.Luno
import za.co.botcoin.model.room.BotCoinDatabase
import za.co.botcoin.model.room.ILunoDao

class LunoRepository(private val application: Application) {
    private val lunoDao: ILunoDao = BotCoinDatabase.getDatabase(application).lunoDao()

    init {
       CoroutineScope(Dispatchers.IO).launch {
           lunoDao.insert(Luno(1, "Luno"))
       }
    }
}