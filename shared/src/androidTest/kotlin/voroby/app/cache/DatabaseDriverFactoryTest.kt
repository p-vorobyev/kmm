package voroby.app.cache

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DatabaseDriverFactoryTest {

    lateinit var driverFactory: DatabaseDriverFactory

    @Before
    fun setUp() {
        val context: Context = mockk()
        driverFactory = DatabaseDriverFactory(context)
    }

    @Test
    fun createDriver() {
        val driver = driverFactory.createDriver()
        assertTrue(driver is AndroidSqliteDriver)
    }
}