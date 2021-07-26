package testing.steven.migo.androidTestImplementation

import androidx.room.TypeConverter
import testing.steven.migo.datamodel.PassType
import java.util.*

class Converters {

    @TypeConverter
    fun toPassType(value: Int) = enumValues<PassType>()[value]

    @TypeConverter
    fun fromPassType(value: PassType) = value.ordinal

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
