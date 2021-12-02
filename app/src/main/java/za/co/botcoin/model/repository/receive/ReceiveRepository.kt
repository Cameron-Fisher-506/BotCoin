package za.co.botcoin.model.repository.receive

import android.app.Application
import za.co.botcoin.model.models.Receive
import za.co.botcoin.model.repository.BaseRepository
import za.co.botcoin.model.room.BotCoinDatabase
import za.co.botcoin.model.room.IReceiveDao
import za.co.botcoin.model.room.upsert
import za.co.botcoin.utils.DataAccessStrategyUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.Resource

class ReceiveRepository(private val application: Application) : BaseRepository() {
    private val receiveDao: IReceiveDao = BotCoinDatabase.getDatabase(application).receiveDao()

    suspend fun receive(asset: String, keyId: String, secretKey: String): Resource<List<Receive>> {
        return DataAccessStrategyUtils.synchronizedCache(
            application,
            { BotCoinDatabase.getResource { receiveDao.getAll() } },
            { botCoinService.receive("Basic ${GeneralUtils.getAuth(keyId, secretKey)}", asset) },
            { receiveDao.upsert(it, receiveDao) }
        )
    }
}