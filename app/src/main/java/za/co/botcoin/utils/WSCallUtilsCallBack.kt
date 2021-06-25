package za.co.botcoin.utils

interface WSCallUtilsCallBack {
    fun taskCompleted(response: String?, reqCode: Int)
}