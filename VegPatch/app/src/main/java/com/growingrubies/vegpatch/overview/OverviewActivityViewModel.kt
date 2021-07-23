package com.growingrubies.vegpatch.overview

import android.app.Application
import androidx.lifecycle.*
import com.growingrubies.vegpatch.data.Plant
import com.growingrubies.vegpatch.data.dto.PlantDTO
import com.growingrubies.vegpatch.data.local.PlantDatabaseDao
import com.growingrubies.vegpatch.data.local.PlantLocalRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import com.growingrubies.vegpatch.data.dto.Result

class OverviewActivityViewModel(
    val database: PlantDatabaseDao,
    application: Application): AndroidViewModel(application) {

    //Encapsulated LiveData

    private val _plantList = MutableLiveData<List<Plant>>()
    val plantList: LiveData<List<Plant>>
        get() = _plantList

    private val _navigateToPlantDetail = MutableLiveData<Long>()
    val navigateToPlantDetail: LiveData<Long>
        get() = _navigateToPlantDetail

    //Use repository instead of ViewModel directly
    private val plantRepository = PlantLocalRepository(database)

    init {
        setPlantList()
        getPlantlist()
    }

    /**
     * Functions interacting with repository for database
     */
    private fun setPlantList() {
        Timber.i("setPlantList called")
        val tempPlantOne = Plant(1L, "Potato")
        val tempPlantTwo = Plant(2L, "Pumpkin")
        val tempPlantThree = Plant(3L, "Broccoli")
        val tempPlantList = arrayListOf(tempPlantOne, tempPlantTwo, tempPlantThree)
        viewModelScope.launch {
            for (plant in tempPlantList) {
//                plantRepository.insert(plant)
                plantRepository.insert(
                    PlantDTO(plant.id,
                        plant.name)
                )
            }
        }
    }

    private fun getPlantlist() {
        Timber.i("getPlantList called")
        viewModelScope.launch {
            val result = plantRepository.getAllPlants()
            when (result) {
                is Result.Success<*> -> {
//                    val dataList = ArrayList<Plant>()
//                    dataList.addAll((result.data as List<Plant>))
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
        _navigateToPlantDetail.value = id
    }

}