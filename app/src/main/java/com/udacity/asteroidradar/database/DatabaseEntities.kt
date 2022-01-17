package com.udacity.asteroidradar.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.udacity.asteroidradar.Asteroid
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "asteroids_table")
data class DatabaseAsteroids constructor(
    @PrimaryKey
    val id: Long,
    //@ColumnInfo(name = "codename")
    val codename: String,
    //@ColumnInfo(name = "close_approach_date")
    val closeApproachDate: String,
    //@ColumnInfo(name = "absolute_magnitude")
    val absoluteMagnitude: Double,
    //@ColumnInfo(name = "estimated_diameter")
    val estimatedDiameter: Double,
    //@ColumnInfo(name = "relative_velocity")
    val relativeVelocity: Double,
    //@ColumnInfo(name = "distance_from_earth")
    val distanceFromEarth: Double,
    //@ColumnInfo(name = "is_potentially_hazardous")
    val isPotentiallyHazardous: Boolean
    ) : Parcelable

//Converts from database objects to domain objects
fun List<DatabaseAsteroids>.asDomainModel() : List<Asteroid>{
    return map{
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