package za.co.botcoin.model.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["timestamp"], unique = true)])
class Candle : BaseModel() {
    var close: String = ""
    var high: String = ""
    var low: String = ""
    var open: String = ""
    @PrimaryKey
    var timestamp: String = ""
    var volume: String = ""
    var accountId: Int = 1
}