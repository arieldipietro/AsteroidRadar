package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

enum class Filter{
    DAY, WEEK, ALL
}

@Dao
interface AsteroidsDao {
    @Query("select * from asteroids_table ORDER BY closeApproachDate DESC")
    fun getAsteroids(): LiveData<List<DatabaseAsteroids>>

    @Query("select * from asteroids_table WHERE closeApproachDate = :today ORDER BY closeApproachDate DESC")
    fun getTodayAsteroids(today: String): LiveData<List<DatabaseAsteroids>>

    @Query("select * from asteroids_table WHERE closeApproachDate >= :today ORDER BY closeApproachDate DESC")
    fun getOnwardsAsteroids(today : String): LiveData<List<DatabaseAsteroids>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids : DatabaseAsteroids)

    //Delete asteroids with data before today
    @Query("DELETE FROM asteroids_table WHERE closeApproachDate < :today")
    fun deletePreviousAsteroids(today : String)
}

@Database(entities = [DatabaseAsteroids::class], version = 1)
abstract class AsteroidsDatabase: RoomDatabase(){
    abstract val asteroidDao : AsteroidsDao
}

private lateinit var INSTANCE : AsteroidsDatabase

fun getDatabase(context: Context) : AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java){
        if(!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                AsteroidsDatabase::class.java,  "asteroids").build()
        }
        return INSTANCE
    }
}

