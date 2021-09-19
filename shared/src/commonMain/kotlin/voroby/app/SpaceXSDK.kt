package voroby.app

import voroby.app.cache.Database
import voroby.app.cache.DatabaseDriverFactory
import voroby.app.dto.RocketLaunch
import voroby.app.network.SpaceXApi

class SpaceXSDK(databaseDriverFactory: DatabaseDriverFactory) {

    private val database = Database(databaseDriverFactory)

    private val api = SpaceXApi()

    /*
    * In swift all exceptions are checked, but in kotlin aren't.
    */
    @Throws(Exception::class)
    suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        val cachedLaunches = database.getAllLaunches()
        return if (cacheNotEmtyCheckReload(cachedLaunches, forceReload)) {
            cachedLaunches
        } else {
            api.getAllLaunches().also {
                database.clearDatabase()
                database.createLaunches(it)
            }
        }
    }

    private fun cacheNotEmtyCheckReload(
        cachedLaunches: List<RocketLaunch>,
        forceReload: Boolean
    ) = cachedLaunches.isNotEmpty() && !forceReload

}