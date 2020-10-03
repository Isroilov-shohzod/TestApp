package tj.isroilov.testapp.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BaseEntity(
    @PrimaryKey
    val id: String,
    val images: List<String>?,
    val type: String?,
    val price: String?,
    val address: String?,
    val station: String?,
    val stationDistance: String?
)