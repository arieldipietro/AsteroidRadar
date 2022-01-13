package com.udacity.asteroidradar.main

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.MainActivity
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.network.AsteroidApi
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _status = MutableLiveData<List<Asteroid>>()
    val status: LiveData<List<Asteroid>>
        get() = _status

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay : LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    init {
        getAsteroids()
        getPictureOfTheDay()
    }


    private fun getAsteroids() {
        AsteroidApi.retrofitServiceAsteroids.getAsteroids().enqueue(object : retrofit2.Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                _status.value =
                    response.body()?.let { parseAsteroidsJsonResult(JSONObject(it))}
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                _error.value = t.message
            }
        })
    }

    private fun getPictureOfTheDay(){
        AsteroidApi.retrofitServicePictureOfDay.getPictureOfTheDay().enqueue(object : retrofit2.Callback<PictureOfDay>{
            override fun onResponse(call: Call<PictureOfDay>, response: Response<PictureOfDay>) {
                try{
                    if(response.body()?.mediaType == "image"){
                        _pictureOfDay.value = response.body()
                    }
                } catch(e: Exception){
                    Log.i("Main Activity", "Image unavailable")
                }
            }

            override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {
                _pictureOfDay.value = null
            }
        })
    }

    }


