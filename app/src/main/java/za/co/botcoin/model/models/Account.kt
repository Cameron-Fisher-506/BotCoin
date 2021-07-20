package za.co.botcoin.model.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["id"], unique = true)])
data class Account(
        @PrimaryKey(autoGenerate = false)
        var id: Int = 1,
        var name: String = ""
) : BaseModel()