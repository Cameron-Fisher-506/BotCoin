package za.co.botcoin.model.repository.postOrder

import android.app.Application
import za.co.botcoin.model.models.PostOrder
import za.co.botcoin.model.repository.BaseRepository
import za.co.botcoin.model.room.BotCoinDatabase
import za.co.botcoin.model.room.IPostOrderDao
import za.co.botcoin.model.room.upsert
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.DataAccessStrategyUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.Resource

class PostOrderRepository(private val application: Application) : BaseRepository() {
    private val postOrderDao: IPostOrderDao = BotCoinDatabase.getDatabase(application).postOrderDao()

    suspend fun postOrder(pair: String, type: String, volume: String, price: String): Resource<List<PostOrder>> {
        return DataAccessStrategyUtils.synchronizedCache(
            application,
            { BotCoinDatabase.getResource { postOrderDao.getAll() } },
            { botCoinService.postOrder("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}", pair, type, volume, price) },
            { postOrderDao.upsert(it, postOrderDao) }
        )
    }
}