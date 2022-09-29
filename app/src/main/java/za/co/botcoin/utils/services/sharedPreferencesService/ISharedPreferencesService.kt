package za.co.botcoin.utils.services.sharedPreferencesService

interface ISharedPreferencesService {
    fun save(key: String, value: String)
    operator fun get(sharedPrefName: String): String?
}