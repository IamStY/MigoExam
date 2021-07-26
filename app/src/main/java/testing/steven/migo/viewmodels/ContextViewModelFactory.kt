package testing.steven.migo.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import testing.steven.migo.androidTestImplementation.AppDatabase

class ContextViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    val passDao = AppDatabase.getDatabase(context.applicationContext).passDao()
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel(context, passDao) as T
        }
        throw  IllegalArgumentException("Unknown ViewModel class")
    }
}
