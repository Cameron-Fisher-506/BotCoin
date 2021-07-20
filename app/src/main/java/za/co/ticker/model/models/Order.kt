package za.co.ticker.model.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(indices = [Index(value = ["id"], unique = true)])
class Order : BaseModel() {
    @PrimaryKey(autoGenerate = false)
    @SerializedName("order_id")
    var id: String = ""
    var type: String = ""
    var state: String = ""
    var counter: String = ""
    var pair: String = ""
    var base: String = ""

    @SerializedName("limit_price")
    var limitPrice: String = ""

    @SerializedName("limit_volume")
    var limitVolume: String = ""

    @SerializedName("fee_counter")
    var feeCounter: String = ""

    @SerializedName("fee_base")
    var feeBase: String = ""

    @SerializedName("expiration_timestamp")
    var expirationTimestamp: String = ""

    @SerializedName("creation_timestamp")
    var createdTime: String = ""

    @SerializedName("completed_timestamp")
    var completedTime: String = ""

    var accountId: Int = 1
}