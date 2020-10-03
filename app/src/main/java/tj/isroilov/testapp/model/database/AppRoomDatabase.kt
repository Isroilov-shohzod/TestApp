package tj.isroilov.testapp.model.database

import android.content.Context
import androidx.room.*
import tj.isroilov.testapp.model.entity.BaseConverter
import tj.isroilov.testapp.model.entity.BaseEntity

@Database(
    entities = [BaseEntity::class],
    version = 1
)

@TypeConverters(BaseConverter::class)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun getBaseDao(): BaseDao

    companion object {

        @Volatile
        private var instance: AppRoomDatabase? = null
        private var LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {
                instance
                    ?: buildDataBase(
                        context
                    ).also {
                        instance = it
                    }
            }

        private fun buildDataBase(context: Context) =
            Room.databaseBuilder(
                context,
                AppRoomDatabase::class.java,
                "AppDataBase.db"
            ).build()

    }


}