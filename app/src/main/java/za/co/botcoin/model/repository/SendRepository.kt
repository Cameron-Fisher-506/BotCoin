package za.co.botcoin.model.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import za.co.botcoin.model.models.Send
import za.co.botcoin.model.room.BotCoinDatabase
import za.co.botcoin.model.room.ISendDao
import za.co.botcoin.model.room.upsert
import za.co.botcoin.model.service.BotCoinService
import za.co.botcoin.utils.DataAccessStrategyUtils
import za.co.botcoin.utils.Resource

class SendRepository(private val application: Application) {
    private val botCoinService: BotCoinService = BotCoinService()
    private val sendDao: ISendDao = BotCoinDatabase.getDatabase(application).sendDao()

    private val mustSendLiveData by lazy { MutableLiveData<Boolean>() }

    fun send(mustSend: Boolean, amount: String, currency: String, address: String, destinationTag: String = ""): LiveData<Resource<List<Send>>> {
        mustSendLiveData.value = mustSend
        return Transformations.switchMap(mustSendLiveData) {
            DataAccessStrategyUtils.synchronizedCache(
                    application,
                    { BotCoinDatabase.getResource { sendDao.getAll() } },
                    { if (destinationTag.isNotBlank()) botCoinService.send(amount, currency, address) else botCoinService.send(amount, currency, address, destinationTag) },
                    { sendDao.upsert(it, sendDao) }
            )
        }
    }
}