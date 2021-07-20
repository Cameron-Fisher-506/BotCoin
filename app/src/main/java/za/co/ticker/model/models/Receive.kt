package za.co.ticker.model.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(indices = [Index(value = ["id"], unique = false)])
class Receive : BaseModel() {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var address: String = ""
    var asset: String = ""
    var name: String = ""

    @SerializedName("account_id")
    var accountId: String = ""

    @SerializedName("assigned_at")
    var assignedAt: String = ""

    @SerializedName("qr_code_uri")
    var qrCodeUri: String = ""

    @SerializedName("receive_fee")
    var receiveFee: String = ""

    @SerializedName("total_received")
    var totalReceived: String = ""

    @SerializedName("total_unconfirmed")
    var totalUnconfirmed: String = ""
}