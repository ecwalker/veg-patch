package com.growingrubies.vegpatch.addplant

import android.app.Application
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growingrubies.vegpatch.data.Plant
import com.growingrubies.vegpatch.data.dto.PlantDTO
import com.growingrubies.vegpatch.data.dto.Result
import com.growingrubies.vegpatch.data.local.PlantDatabaseDao
import com.growingrubies.vegpatch.data.local.PlantLocalRepository
import com.growingrubies.vegpatch.data.local.WeatherDatabaseDao
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.Exception

class AddPlantViewModel(val plantDao: PlantDatabaseDao,
                        val weatherDao: WeatherDatabaseDao,
                        application: Application
): AndroidViewModel(application) {

    //Encapsulated LiveData

    private val _plantList = MutableLiveData<List<Plant>>()
    val plantList: LiveData<List<Plant>>
        get() = _plantList

    private val _navigateToMainActivity = MutableLiveData<Boolean>()
    val navigateToMainActivity: LiveData<Boolean>
        get() = _navigateToMainActivity

    //Use repository instead of ViewModel directly
    private val plantRepository = PlantLocalRepository(plantDao, weatherDao)

    init {
        getPlantlist()
    }

    /**
     * Functions interacting with repository for database
     */
    private fun setActivePlant(id: Long) {
        Timber.i("setPlantList called")
        viewModelScope.launch {
            try {
                plantDao.setPlantActive(id)
            } catch (e: Exception) {
                Timber.i("Exception: ${e.message}")
            }
        }
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
                            plant.category,
                            plant.icon,
                            plant.isAnnual,
                            plant.isFrostHardy,
                            plant.isGreenhousePlant,
                            plant.sowDate,
                            plant.plantDate,
                            plant.harvestDate,
                            plant.isActive
                        )
                    })
                    _plantList.value = dataList
                    Timber.i("plantList returned from database")
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
        setActivePlant(id)
        _navigateToMainActivity.value = true
    }

}