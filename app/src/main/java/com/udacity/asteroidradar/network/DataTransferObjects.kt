package com.udacity.asteroidradar.network

import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.DatabaseAsteroids

@JsonClass(generateAdapter = true)
data class NetworkAsteroidsContainer(val asteroids: List<NetworkAsteroids>)

@JsonClass(generateAdapter = true)
data class NetworkAsteroids(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

//converts network results to database objects
fun NetworkAsteroidsContainer.asDomainModel(): List<Asteroid>{
    return asteroids.map{
        Asteroid(
            id= it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}


    //converts from data transfer objects to database objects
fun List<Asteroid>.asDatabaseModel(): Array<DatabaseAsteroids>{
    return map{
        DatabaseAsteroids(
            id= it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}
