package voroby.app.cache

import voroby.app.dto.Links
import voroby.app.dto.Rocket
import voroby.app.dto.RocketLaunch
import vorobyappcache.AppDatabaseQueries

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {

    private val database: AppDatabase = AppDatabase(databaseDriverFactory.createDriver())

    private val dbQueries: AppDatabaseQueries = database.appDatabaseQueries

    internal fun clearDatabase() {
        dbQueries.transaction {
            dbQueries.removeAllRockets()
            dbQueries.removeAllLaunches()
        }
    }

    internal fun getAllLaunches(): List<RocketLaunch> {
        return dbQueries.selectAllLaunchesInfo(::rocketLaunchMapper).executeAsList()
    }

    private fun rocketLaunchMapper(
        flightNumber: Long,
        missionName: String,
        launchYear: Int,
        rocketId: String,
        details: String?,
        launchSuccess: Boolean?,
        launchDateUTC: String,
        missionPatchUrl: String?,
        articleUrl: String?,
        rocket_id: String?,
        name: String?,
        type: String?
    ): RocketLaunch {
        return RocketLaunch(
            flightNumber = flightNumber.toInt(),
            missionName = missionName,
            launchYear = launchYear,
            launchDateUTC = launchDateUTC,
            details = details,
            launchSuccess = launchSuccess,
            rocket = Rocket(id = rocketId, name = name!!, type = type!!),
            links = Links(missionPatchUrl = missionPatchUrl, articleUrl = articleUrl)
        )
    }

    internal fun createLaunches(launches: List<RocketLaunch>) {
        dbQueries.transaction {
            launches.forEach { launch ->
                val rocket: vorobyappcache.Rocket? =
                    dbQueries.selectRocketById(launch.rocket.id).executeAsOneOrNull()
                if (rocket == null) {
                    insertRocket(launch)
                }
                insertLaunch(launch)
            }
        }
    }

    private fun insertRocket(launch: RocketLaunch) {
        val rocket = launch.rocket
        dbQueries.insertRocket(rocket.id, rocket.name, rocket.type)
    }

    private fun insertLaunch(launch: RocketLaunch) {
        val links: Links = launch.links
        dbQueries.insertLaunch(
            flightNumber = launch.flightNumber.toLong(),
            missionName = launch.missionName,
            launchYear = launch.launchYear,
            rocketId = launch.rocket.id,
            details = launch.details,
            launchSuccess = launch.launchSuccess ?: false,
            launchDateUTC = launch.launchDateUTC,
            missionPatchUrl = links.missionPatchUrl,
            articleUrl = links.articleUrl
        )
    }
}