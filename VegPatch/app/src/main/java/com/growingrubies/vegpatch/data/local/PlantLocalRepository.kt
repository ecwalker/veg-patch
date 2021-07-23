package com.growingrubies.vegpatch.data.local

import com.growingrubies.vegpatch.data.Plant
import com.growingrubies.vegpatch.data.dto.PlantDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import com.growingrubies.vegpatch.data.dto.Result

class PlantLocalRepository(private val database: PlantDatabaseDao) {

    suspend fun insert(plant: PlantDTO) {
        Timber.i("insert called")
        withContext(Dispatchers.IO) {
            try {
                database.insert(plant)
            } catch (e: Exception) {
                Timber.i("Insert failed: ${e.message}")
            }
        }
    }

//    suspend fun getAllPlants(): Result<List<Plant>> = withContext(Dispatchers.IO) {
//        Timber.i("getAllPlants called")
//        return@withContext try {
//            Result.Success(database.getAll())
//        } catch (e: Exception) {
//            Timber.i("Error: ${e.message}")
//            Result.Error(e.localizedMessage)
//        }
//    }

    suspend fun getAllPlants(): Result<List<PlantDTO>> = withContext(Dispatchers.IO) {
        Timber.i("getAllPlants called")
        return@withContext try {
            Result.Success(database.getAll())
        } catch (e: Exception) {
            Timber.i("Error: ${e.message}")
            Result.Error(e.localizedMessage)
        }
    }

}