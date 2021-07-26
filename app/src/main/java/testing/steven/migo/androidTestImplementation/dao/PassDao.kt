package testing.steven.migo.androidTestImplementation.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import testing.steven.migo.datamodel.PassDataBean

@Dao
interface PassDao {
    @Query("SELECT * FROM passes_table ORDER BY type ,expiredTime ASC")
    fun getCurrentPasses(): LiveData<List<PassDataBean>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPass(passDataBean: PassDataBean)

    @Query("SELECT * FROM passes_table ORDER BY expiredTime DESC limit 1")
    suspend fun getPassByLastAccountExpireDate(): PassDataBean

    @Update(entity = PassDataBean::class)
    fun updatePass(pass: PassDataBean)

    @Query("SELECT * FROM passes_table WHERE id == :id")
    fun getPass(id: Int): PassDataBean?
}
