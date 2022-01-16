package com.udacity.asteroidradar.main

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.MainActivity
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class MainViewModel(application : Application) : AndroidViewModel(application) {

    //Repository
    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    private val _status = MutableLiveData<List<Asteroid>>()
    val status: LiveData<List<Asteroid>>
        get() = _status

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay : LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _navigateToDetailFragment = MutableLiveData<Asteroid>()
    val navigateToDetailFragment : LiveData<Asteroid>
        get() = _navigateToDetailFragment

    init {
        viewModelScope.launch {
            asteroidsRepository.refreshAsteroids()
        }
        getPictureOfTheDay()
    }


    /*private fun getAsteroids() {
        AsteroidApi.retrofitServiceAsteroids.getAsteroids().enqueue(object : retrofit2.Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                _status.value =
                    response.body()?.let { parseAsteroidsJsonResult(JSONObject(it))}
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                _error.value = t.message
            }
        })
    }*/

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

    fun onAsteroidClicked(asteroid: Asteroid){
        _navigateToDetailFragment.value = asteroid
    }

    fun onDetailsFragmentNavigated(){
        _navigateToDetailFragment.value = null
    }

}

