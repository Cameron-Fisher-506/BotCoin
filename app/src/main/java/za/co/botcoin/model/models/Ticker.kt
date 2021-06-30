package za.co.botcoin.model.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(indices = [Index(value = ["pair"], unique = true)])
class Ticker : BaseModel() {
    @PrimaryKey(autoGenerate = false)
    var pair: String = ""
    var ask: String = ""
    var bid: String = ""
    var status: String = ""
    var timestamp: String = ""
    var lunoId: Int = 1

    @SerializedName("last_trade")
    var lastTrade: String = ""

    @SerializedName("rolling_24_hour_volume")
    var rollingTwentyFourHourVolume: String = ""
}