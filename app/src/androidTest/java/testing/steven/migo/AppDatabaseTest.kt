package testing.steven.migo

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import testing.steven.migo.androidTestImplementation.AppDatabase
import testing.steven.migo.androidTestImplementation.dao.PassDao
import testing.steven.migo.datamodel.PassDataBean
import testing.steven.migo.datamodel.PassType
import java.io.IOException
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


@RunWith(AndroidJUnit4::class)
class AppDatabaseTest : TestCase() {
    private lateinit var passDao: PassDao
    private lateinit var db: AppDatabase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
                context, AppDatabase::class.java
        ).setTransactionExecutor(Executors.newSingleThreadExecutor()).build()
        passDao = db.passDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun getCurrentPasses_assertThatObjectEquals_insertAndCheckIfListResultContains() = runBlocking {
        val passBean = PassDataBean(id = 1, name = "testName", count = 5, insertionTime = Calendar.getInstance().time, type = PassType.DAILY)
        passDao.insertPass(passBean)
        val dbPassBean = passDao.getCurrentPasses().getOrAwaitValue()
        assertThat(dbPassBean[0], equalTo(passBean))
    }

    @Test
    @Throws(Exception::class)
    fun getPassByLastAccountExpireDate_assertThatObjectEquals_insertAndCheckIfLatestExpiredTimeResultExpected() = runBlocking {
        val passBean = PassDataBean(id = 2, name = "testName", count = 5, expiredTime = Calendar.getInstance().time, type = PassType.DAILY)
        val latestCalendar = Calendar.getInstance()
        latestCalendar.add(Calendar.DAY_OF_MONTH, 1)

        val passBeanLatest = PassDataBean(id = 1, name = "testLatest", count = 5, expiredTime = latestCalendar.time, type = PassType.HOURLY)

        passDao.insertPass(passBean)

        passDao.insertPass(passBeanLatest)

        val result = passDao.getPassByLastAccountExpireDate()


        assertThat(result, equalTo(passBeanLatest))
    }

    fun <T> LiveData<T>.getOrAwaitValue(
            time: Long = 2,
            timeUnit: TimeUnit = TimeUnit.SECONDS
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : androidx.lifecycle.Observer<T> {
            override fun onChanged(o: T?) {
                data = o
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }
        this.observeForever(observer)

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

        @Suppress("UNCHECKED_CAST")
        return data as T
    }
}
