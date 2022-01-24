package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.DatabaseAsteroids
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.network.sevenDaysOnwards
import com.udacity.asteroidradar.network.today
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

//enum class for filter values
enum class AsteroidsFilter{SHOW_TODAY, SHOW_ONWARDS, SHOW_SAVED}

class MainViewModel(application : Application) : AndroidViewModel(application) {

    //Repository
    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    //filter
    private val asteroidsFilter = MutableLiveData(AsteroidsFilter.SHOW_TODAY)

    //Setting up live data for the observer
    //private val _status = MutableLiveData<List<DatabaseAsteroids>>()
    val status : LiveData<List<DatabaseAsteroids>> = Transformations.switchMap(asteroidsFilter){
        filter ->
        when(filter){
            AsteroidsFilter.SHOW_TODAY -> database.asteroidDao.getTodayAsteroids(today)
            AsteroidsFilter.SHOW_ONWARDS -> database.asteroidDao.getOnwardsAsteroids(today)
            else -> {
                database.asteroidDao.getAsteroids()
            }
        }
    }

    fun updateFilter(filter: AsteroidsFilter) {
        asteroidsFilter.value =  filter
    }

    //getting filtered Asteroids
    private fun getTodayAsteroids(){
        //_status.value = database.asteroidDao.getTodayAsteroids(today)
        Log.i("MainActivity", "getToday function called")
    }
    private fun getOnwardsAsteroids(){
        //_status.value = database.asteroidDao.getOnwardsAsteroids(today)
    }
    private fun getAllAsteroids(){
        //_status.value = database.asteroidDao.getAsteroids()
    }

    //getting picture of the day
    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay : LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val _navigateToDetailFragment = MutableLiveData<DatabaseAsteroids>()
    val navigateToDetailFragment : LiveData<DatabaseAsteroids>
        get() = _navigateToDetailFragment

    init {
        viewModelScope.launch {
            asteroidsRepository.refreshAsteroids()
        }
        getAllAsteroids()
        getPictureOfTheDay()

        //checking up dates match current date
        Log.i("MainActivity", "Today's date: $today")
        Log.i("MainActivity", "Seven days onwards: $sevenDaysOnwards")

        //no longer needed, since this is done with the work manager
        //database.asteroidDao.deletePreviousAsteroids(today)
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

    fun onAsteroidClicked(asteroid: DatabaseAsteroids){
        _navigateToDetailFragment.value = asteroid
    }

    fun onDetailsFragmentNavigated(){
        _navigateToDetailFragment.value = null
    }

}

