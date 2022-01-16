package com.udacity.asteroidradar.network

import android.media.Image
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//using scalars converter factory to get the list of asteroids from the internet
object Network {
    val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(Constants.BASE_URL)
        .build()

    val asteroidsRadar = retrofit.create(AsteroidApiService::class.java)
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//using moshi to get the picture of the day from the internet
private val retrofitPictureOfDay = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()


interface AsteroidApiService {

    @GET("neo/rest/v1/feed")
    fun getAsteroids(
        @Query("start_date") startDate: String = "2015-09-07",
        @Query("end_date") endDate : String = "2015-09-08",
        @Query("api_key") apiKey: String = API_KEY
    ) : String
}

interface PictureOfDayApiService{

    @GET("planetary/apod")
    fun getPictureOfTheDay(
        @Query("api_key") apiKey: String = API_KEY
    ) : Call<PictureOfDay>
}

object AsteroidApi{
    val retrofitServiceAsteroids : AsteroidApiService by lazy{
        Network.retrofit.create(AsteroidApiService::class.java)
    }
    val retrofitServicePictureOfDay : PictureOfDayApiService by lazy{
        retrofitPictureOfDay.create(PictureOfDayApiService::class.java)
    }
}



