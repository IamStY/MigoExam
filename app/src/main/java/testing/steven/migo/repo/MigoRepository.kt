package testing.steven.migo.repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.android.volley.NoConnectionError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import testing.steven.migo.api.ApiRequestManager
import testing.steven.migo.database.dao.PassDao
import testing.steven.migo.datamodel.MigoResponse
import testing.steven.migo.datamodel.PassDataBean
import testing.steven.migo.datamodel.PassType
import java.util.*

class MigoRepository(
        private val context: Context, private val vmScope: CoroutineScope,
        private val passDao: PassDao
) {
    val passes = passDao.getCurrentPasses()
    val repoMigoResponse = MutableLiveData<String>()

    init {
        fireFetchServerData()
    }

    fun insertPass(data: PassDataBean) {
        vmScope.launch(Dispatchers.IO) {
            passDao.insertPass(data)
        }
    }

    fun activePassStatus(passId: Int) {
        vmScope.launch(Dispatchers.IO) {
            val latestExpiry = passDao.getPassByLastAccountExpireDate().expiredTime
            val updateActivateDate: Date
            val updateExpiryDate: Date
            val dbPass = passDao.getPass(passId)
            dbPass ?: return@launch
            if (latestExpiry == null || latestExpiry.time == 0L) {
                val currentCalendar = Calendar.getInstance()
                updateActivateDate = currentCalendar.time
                val passType = dbPass.type
                updateExpiryDate = if (passType == PassType.DAILY) {
                    currentCalendar.add(Calendar.DAY_OF_MONTH, dbPass.count)
                    currentCalendar.time
                } else {
                    currentCalendar.add(Calendar.HOUR_OF_DAY, dbPass.count)
                    currentCalendar.time
                }

            } else {
                val expiryCalendar = Calendar.getInstance()
                expiryCalendar.time = latestExpiry

                updateActivateDate = expiryCalendar.time
                val passType = dbPass.type
                updateExpiryDate = if (passType == PassType.DAILY) {
                    expiryCalendar.add(Calendar.DAY_OF_MONTH, dbPass.count)
                    expiryCalendar.time
                } else {
                    expiryCalendar.add(Calendar.HOUR_OF_DAY, dbPass.count)
                    expiryCalendar.time

                }
            }
            dbPass.expiredTime = updateExpiryDate
            dbPass.activatedTime = updateActivateDate
            passDao.updatePass(dbPass)
        }
    }

    private fun fireFetchServerData(directlyRequestPublic: Boolean = false) {
        vmScope.launch(Dispatchers.IO) {
            val dataResponse = ApiRequestManager.fetchServerData(context, directlyRequestPublic)
            if (hasPrivacyException(dataResponse)) {
                fireFetchServerData(directlyRequestPublic = true)
                return@launch
            }
            repoMigoResponse.postValue(dataResponse.responseString)
        }
    }

    private fun hasPrivacyException(dataResponse: MigoResponse): Boolean {
        return dataResponse.exception != null && dataResponse.exception is NoConnectionError
    }
}
