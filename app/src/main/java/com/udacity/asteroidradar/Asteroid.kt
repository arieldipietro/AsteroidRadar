package com.udacity.asteroidradar

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Asteroid(val id: Long,
                    val codename: String,
                    val closeApproachDate: String,

                    @Json(name="absolute_magnitude") val absoluteMagnitude: Double,

                    @Json(name="estimated_diameter_max")
                    val estimatedDiameter: Double,
                    @Json(name="kilometers_per_second")val relativeVelocity: Double,

                    @Json(name="astronomical")val distanceFromEarth: Double,

                    @Json(name = "is_potentially_hazardous_asteroid")
                    val isPotentiallyHazardous: Boolean)
    : Parcelable