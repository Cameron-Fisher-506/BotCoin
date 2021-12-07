package za.co.botcoin.model.repository.send

import android.app.Application
import za.co.botcoin.model.models.Send
import za.co.botcoin.model.repository.BaseRepository
import za.co.botcoin.model.room.BotCoinDatabase
import za.co.botcoin.model.room.ISendDao
import za.co.botcoin.model.room.upsert
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.DataAccessStrategyUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.Resource

class SendRepository(private val application: Application) : BaseRepository() {
    private val sendDao: ISendDao = BotCoinDatabase.getDatabase(application).sendDao()

    suspend fun send(amount: String, currency: String, address: String, destinationTag: String = ""): Resource<List<Send>> {
        return DataAccessStrategyUtils.synchronizedCache(
            application,
            { BotCoinDatabase.getResource { sendDao.getAll() } },
            {
                if (destinationTag.isNotBlank()) botCoinService.send("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}", amount, currency, address)
                else botCoinService.send("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}", amount, currency, address, destinationTag)
            },
            { sendDao.upsert(it, sendDao) }
        )
    }
}