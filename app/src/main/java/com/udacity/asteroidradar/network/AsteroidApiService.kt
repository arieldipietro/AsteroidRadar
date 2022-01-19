package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


//using scalars converter factory to get the list of asteroids from the internet
object Network {

    var okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
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

val next7DaysFormattedDates: List<String>
    get() = getNextSevenDaysFormattedDates()

val today = next7DaysFormattedDates[0]
val sevenDaysOnwards = next7DaysFormattedDates[6]

interface AsteroidApiService {

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_date") startDate: String = today,
        @Query("end_date") endDate : String = sevenDaysOnwards,
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



