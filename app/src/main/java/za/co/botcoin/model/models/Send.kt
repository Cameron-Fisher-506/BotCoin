package za.co.botcoin.model.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(indices = [Index(value = ["withdrawalId"], unique = false)])
class Send {
    val success: Boolean = false

    @PrimaryKey(autoGenerate = false)
    @SerializedName("withdrawal_id")
    val withdrawalId: String = ""
}