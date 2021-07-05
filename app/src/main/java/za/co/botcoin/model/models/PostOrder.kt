package za.co.botcoin.model.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(indices = [Index(value = ["id"], unique = true)])
class PostOrder : BaseModel() {
    @SerializedName("order_id")
    @PrimaryKey(autoGenerate = false)
    var id: String = ""
}