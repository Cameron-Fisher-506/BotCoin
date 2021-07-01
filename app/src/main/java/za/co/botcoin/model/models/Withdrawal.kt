package za.co.botcoin.model.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(indices = [Index(value = ["id"], unique = false)])
class Withdrawal : BaseModel() {
    @PrimaryKey(autoGenerate = false)
    var id: String = ""
    var amount: String = ""
    var currency: String = ""
    var fee: String = ""
    var status: String = ""
    var type: String = ""

    @SerializedName("created_at")
    var createdAt: String = ""

    @SerializedName("external_id")
    var externalId: String = ""
}