package tj.isroilov.testapp.model.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tj.isroilov.testapp.model.entity.BaseEntity

@Dao
interface BaseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveEntity(entities: List<BaseEntity>)

    @Query("SELECT * FROM baseentity")
    fun readEntity(): LiveData<List<BaseEntity>>

}