package com.udacity.asteroidradar.repository

import android.net.Network
import android.util.Log
import android.view.animation.Transformation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.Transformations.map
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.NetworkAsteroids
import com.udacity.asteroidradar.network.NetworkAsteroidsContainer
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject


class AsteroidsRepository   (private val database : AsteroidsDatabase) {

    //loading asteroids from the offline cache
    val asteroids : LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroids()){
        it.asDomainModel()
    }


    //Refreshing the offline cache from a coroutine
    suspend fun refreshAsteroids(){
        withContext(Dispatchers.IO) {
            try {
                val asteroidsResponse = com.udacity.asteroidradar.network.Network
                    .asteroidsRadar.getAsteroids()

                val asteroidsList = parseAsteroidsJsonResult(JSONObject(asteroidsResponse))

                database.asteroidDao.insertAll(*asteroidsList.asDatabaseModel())
                Log.i("Main Activity", "Fetching succeeded!")

            } catch (e: Exception) {
                Log.i("Main Activity", "Error on getting asteroids: ${e.message}")
            }

        }
    }
}