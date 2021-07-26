package testing.steven.migo.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import testing.steven.migo.MigoUtilities
import testing.steven.migo.R
import testing.steven.migo.androidTestImplementation.dao.PassDao
import testing.steven.migo.datamodel.AdapterType
import testing.steven.migo.datamodel.PassDataBean
import testing.steven.migo.datamodel.PassDataUIModel
import testing.steven.migo.datamodel.PassType
import testing.steven.migo.repo.MigoRepository
import java.util.*
import kotlin.collections.ArrayList

class MainActivityViewModel(context: Context, passDao: PassDao) : ViewModel() {
    private val repository: MigoRepository = MigoRepository(context, viewModelScope, passDao)
    val migoPasses: LiveData<List<PassDataBean>> = repository.passes
    val migoResponse: LiveData<String> = repository.repoMigoResponse
    var adapterListData = MediatorLiveData<ArrayList<PassDataUIModel>>().apply {
        addSource(migoPasses) { passDataBean ->
            val rawAdapterData = passDataBean.map { passDataModel ->
                passDataModel.adapt()
            }.toCollection(ArrayList())
            value = MigoUtilities.generateMigoPassesUIModel(rawAdapterData)
        }
    }

    private fun PassDataBean.adapt(): PassDataUIModel {
        return PassDataUIModel(
                id = id,
                name = name,
                activeTime = activatedTime,
                insertionTime = insertionTime,
                expiredTime = expiredTime,
                type = type,
                adapterType = AdapterType.CONTENT
        )
    }

    fun insertPassToDatabase(context: Context, amount: Int, passType: PassType) {
        val unit = if (passType == PassType.HOURLY) {
            context.getString(R.string.hourly_pass)
        } else {
            context.getString(R.string.day_pass)
        }
        repository.insertPass(
                PassDataBean(
                        name = "$amount $unit",
                        type = passType,
                        count = amount,
                        insertionTime = Calendar.getInstance().time
                )
        )
    }

    fun activatePass(passId: Int) {
        repository.activePassStatus(passId)
    }
}
