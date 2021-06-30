package za.co.botcoin.model.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["asset"], unique = true)])
class Balance : BaseModel() {
    @PrimaryKey(autoGenerate = false)
    var asset: String = ""
    var accountId: String = ""
    var balance: String = ""
    var name: String = ""
    var reserved: String = ""
    var unconfirmed: String = ""
    var lunoAccountId: Int = 1
}