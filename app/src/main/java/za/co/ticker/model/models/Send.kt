package za.co.ticker.model.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(indices = [Index(value = ["withdrawalId"], unique = false)])
class Send {
    var success: Boolean = false

    @PrimaryKey(autoGenerate = false)
    @SerializedName("withdrawal_id")
    var withdrawalId: String = ""
}