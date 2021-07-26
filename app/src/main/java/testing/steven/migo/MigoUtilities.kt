package testing.steven.migo

import testing.steven.migo.datamodel.AdapterType
import testing.steven.migo.datamodel.PassDataUIModel
import testing.steven.migo.datamodel.PassType

object MigoUtilities {
    fun generateMigoPassesUIModel(inputRawDataList: ArrayList<PassDataUIModel>): ArrayList<PassDataUIModel> {
        val dailyData = inputRawDataList.filter { it.type == PassType.DAILY }
        val hourlyData = inputRawDataList.filter { it.type == PassType.HOURLY }
        val uiResultData = ArrayList<PassDataUIModel>()
        if (dailyData.isEmpty().not()) {
            uiResultData.add(PassDataUIModel(adapterType = AdapterType.DAYS_HEADER))
            uiResultData.addAll(dailyData)
        }
        if (hourlyData.isEmpty().not()) {
            uiResultData.add(PassDataUIModel(adapterType = AdapterType.HOUR_HEADER))
            uiResultData.addAll(hourlyData)
        }
        return uiResultData
    }
}
