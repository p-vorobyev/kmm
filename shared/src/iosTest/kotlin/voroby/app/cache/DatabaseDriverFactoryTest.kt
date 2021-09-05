package voroby.app.cache

import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import kotlin.test.Test

class DatabaseDriverFactoryTest {

    val driverFactory: DatabaseDriverFactory = DatabaseDriverFactory()

    @Test
    fun createDriver() {
        val driver = driverFactory.createDriver()
        assert(driver is NativeSqliteDriver)
    }

}