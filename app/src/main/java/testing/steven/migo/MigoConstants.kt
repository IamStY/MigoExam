package testing.steven.migo

import java.text.SimpleDateFormat
import java.util.*

class MigoConstants {
    companion object {
        const val PASS_DATA_BUNDLE_KEY = "pass_data_key"
        const val PUBLIC_API_PATH =
            "https:code-test.migoinc-dev.com/status"
        const val PRIVATE_API_PATH =
            "http://192.168.2.2/status"
        val DISPLAY_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    }

}
