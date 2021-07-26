package testing.steven.migo.datamodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class PassDataUIModel(
        var id: Int = -1,
        var name: String? = null,
        var activeTime: Date? = null,
        var insertionTime: Date? = null,
        var expiredTime: Date? = null,
        var type: PassType? = null,
        var adapterType: AdapterType
) : Parcelable

enum class AdapterType {
    HOUR_HEADER, DAYS_HEADER, CONTENT
}
