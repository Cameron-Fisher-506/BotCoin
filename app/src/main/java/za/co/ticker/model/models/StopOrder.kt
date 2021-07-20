package za.co.ticker.model.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["id"], unique = true)])
class StopOrder : BaseModel() {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var success: Boolean = false
}