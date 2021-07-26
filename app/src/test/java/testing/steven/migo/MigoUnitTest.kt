package testing.steven.migo

import org.junit.Test
import testing.steven.migo.datamodel.AdapterType
import testing.steven.migo.datamodel.PassDataUIModel
import testing.steven.migo.datamodel.PassType
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.assertEquals

class MigoUnitTest {
    private val fakeRawList = ArrayList<PassDataUIModel>().apply {
        add(
                PassDataUIModel(
                        0,
                        "item1",
                        activeTime = null,
                        insertionTime = Calendar.getInstance().time,
                        expiredTime = null,
                        PassType.HOURLY,
                        adapterType = AdapterType.CONTENT
                )

        )
        add(
                PassDataUIModel(
                        0,
                        "item2",
                        activeTime = null,
                        insertionTime = Calendar.getInstance().time,
                        expiredTime = null,
                        PassType.DAILY,
                        adapterType = AdapterType.CONTENT
                )
        )

    }

    @Test
    fun generateMigoPassesUIModel_AssertEqualsTrue_ShouldFindDailyAdapterHeaderData() {
        val resultUIData = MigoUtilities.generateMigoPassesUIModel(fakeRawList)
        assertEquals(
                true,
                (resultUIData.find { it.adapterType == AdapterType.DAYS_HEADER } != null)
        )
    }

    @Test
    fun generateMigoPassesUIModel_AssertEqualsTrue_ShouldFindHourlyAdapterHeaderData() {
        val resultUIData = MigoUtilities.generateMigoPassesUIModel(fakeRawList)
        assertEquals(
                true,
                (resultUIData.find { it.adapterType == AdapterType.HOUR_HEADER } != null)
        )
    }

}
