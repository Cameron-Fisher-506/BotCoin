package za.co.botcoin.model.repository.account

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import za.co.botcoin.model.models.Account
import za.co.botcoin.model.repository.BaseRepository
import za.co.botcoin.model.room.BotCoinDatabase
import za.co.botcoin.model.room.IAccountDao
import javax.inject.Inject

class AccountRepository @Inject constructor(application: Application) : BaseRepository() {
    private val accountDao: IAccountDao = BotCoinDatabase.getDatabase(application).accountDao()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            accountDao.insert(Account(1, "Luno Account"))
        }
    }
}