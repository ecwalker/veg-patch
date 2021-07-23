package com.growingrubies.vegpatch.addplant

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growingrubies.vegpatch.data.Plant
import com.growingrubies.vegpatch.data.dto.PlantDTO
import com.growingrubies.vegpatch.data.dto.Result
import com.growingrubies.vegpatch.data.local.PlantDatabaseDao
import com.growingrubies.vegpatch.data.local.PlantLocalRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class AddPlantViewModel(val database: PlantDatabaseDao,
                        application: Application
): AndroidViewModel(application) {

    //Encapsulated LiveData

    private val _plantList = MutableLiveData<List<Plant>>()
    val plantList: LiveData<List<Plant>>
        get() = _plantList


    //Use repository instead of ViewModel directly
    private val plantRepository = PlantLocalRepository(database)

    init {
        getPlantlist()
    }

    /**
     * Functions interacting with repository for database
     */
    private fun setActivePlant() {
        Timber.i("setPlantList called")
        //TODO: function to set plant as active
    }

    private fun getPlantlist() {
        Timber.i("getPlantList called")
        viewModelScope.launch {
            val result = plantRepository.getAllPlants()
            when (result) {
                is Result.Success<*> -> {
                    val dataList = ArrayList<Plant>()
                    dataList.addAll((result.data as List<PlantDTO>).map { plant ->
                        //map the reminder data from the DB to the be ready to be displayed on the UI
                        Plant(
                            plant.id,
                            plant.name,
                        )
                    })
                    _plantList.value = dataList
                }
                is Result.Error -> {
                    Timber.i("getPlantList failed ${result.message}")
                }
            }

        }
    }

    /**
     * Navigation functions
     */

    fun onPlantClicked(id: Long) {
        //TODO: Action to confirm selection
    }
}