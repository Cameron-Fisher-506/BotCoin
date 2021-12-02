package za.co.botcoin.model.repository

import za.co.botcoin.model.service.BotCoinService

abstract class BaseRepository {
    protected val botCoinService: BotCoinService = BotCoinService()
}