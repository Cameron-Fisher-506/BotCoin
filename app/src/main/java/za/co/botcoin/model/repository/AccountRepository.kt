package za.co.botcoin.model.repository

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import za.co.botcoin.model.models.Account
import za.co.botcoin.model.room.BotCoinDatabase
import za.co.botcoin.model.room.IAccountDao

class AccountRepository(private val application: Application) {
    private val accountDao: IAccountDao = BotCoinDatabase.getDatabase(application).accountDao()

    init {
       CoroutineScope(Dispatchers.IO).launch {
           accountDao.insert(Account(1, "Luno Account"))
       }
    }
}