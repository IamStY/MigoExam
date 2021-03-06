package testing.steven.migo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import testing.steven.migo.database.dao.PassDao
import testing.steven.migo.datamodel.PassDataBean

@TypeConverters(Converters::class)
@Database(entities = [PassDataBean::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun passDao(): PassDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context
        ): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "application_database"
                )

                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
