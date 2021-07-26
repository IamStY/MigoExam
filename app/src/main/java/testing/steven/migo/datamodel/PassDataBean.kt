package testing.steven.migo.datamodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import testing.steven.migo.database.Converters
import java.util.*

@Entity(tableName = "passes_table")
data class PassDataBean(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo(name = "name") var name: String,
        @ColumnInfo(name = "count") var count: Int = 0,
        @ColumnInfo(name = "activatedTime") var activatedTime: Date? = null,
        @ColumnInfo(name = "insertionTime") var insertionTime: Date? = null,
        @ColumnInfo(name = "expiredTime") var expiredTime: Date? = null,
        @TypeConverters(Converters::class) @ColumnInfo(name = "type") var type: PassType
)
