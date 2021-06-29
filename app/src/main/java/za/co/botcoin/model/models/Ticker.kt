package za.co.botcoin.model.models

import com.google.gson.annotations.SerializedName

class Ticker : BaseModel() {
    var ask: String = ""
    var bid: String = ""
    var status: String = ""
    var timestamp: String = ""
    var pair: String = ""

    @SerializedName("last_trade")
    var lastTrade: String = ""

    @SerializedName("rolling_24_hour_volume")
    var rollingTwentyFourHourVolume: String = ""
}