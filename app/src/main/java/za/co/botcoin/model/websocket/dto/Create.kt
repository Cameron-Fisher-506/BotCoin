package za.co.botcoin.model.websocket.dto

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["orderId"], unique = true)])
class Create {
    @PrimaryKey(autoGenerate = false)
    var orderId: String = ""
    var type: String = ""
    var price: String = ""
    var volume: String = ""
}