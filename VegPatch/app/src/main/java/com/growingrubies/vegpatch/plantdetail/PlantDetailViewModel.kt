package com.growingrubies.vegpatch.plantdetail

import android.app.Application
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growingrubies.vegpatch.R
import com.growingrubies.vegpatch.data.Plant
import com.growingrubies.vegpatch.data.dto.PlantDTO
import com.growingrubies.vegpatch.data.dto.Result
import com.growingrubies.vegpatch.data.local.PlantDatabaseDao
import com.growingrubies.vegpatch.data.local.PlantLocalRepository
import com.growingrubies.vegpatch.data.local.WeatherDatabaseDao
import kotlinx.coroutines.launch
import timber.log.Timber

class PlantDetailViewModel(val plantDao: PlantDatabaseDao,
                           val weatherDao: WeatherDatabaseDao,
                           application: Application
): AndroidViewModel(application) {

    //Encapsulated LiveData
    private val _currentPlant = MutableLiveData<Plant>()
    val currentPlant: LiveData<Plant>
        get() = _currentPlant


    //Use repository instead of ViewModel directly
    private val plantRepository = PlantLocalRepository(plantDao, weatherDao)

    init {

    }

    /**
     * Functions interacting with repository for database
     */
    fun getPlantFromDatabase(id: Long) {
        viewModelScope.launch {
            try {
                val result = plantRepository.getCurrentPlant(id)
                when (result) {
                    is Result.Success<*> -> {
                        val data = result.data as PlantDTO
                        _currentPlant.value = Plant(
                            data.id,
                            data.name,
                            data.icon,
                            data.annual,
                            data.frostHardy,
                            data.sowDate,
                            data.plantDate,
                            data.harvestDate,
                            data.active
                        )
                    }
                    is Result.Error -> {
                        Timber.i("Result object error: ${result.message}")
                    }
                }
            } catch (e: Exception) {
                Timber.i("Exception: ${e.message}")
            }
        }
    }

    //TODO: Implement a delete plant function

    /**
     * Navigation functions
     */

    /**
     * Function too map plant icons to name of current plant and set the ImageView resource
     */

    fun mapImages(imgView: ImageView, plant: Plant) {
        Timber.i("mapImages called")
        imgView.apply {
            when (plant.name) {
                "Potato" -> setImageResource(R.drawable._01_potatos)
                "Pumpkin" ->setImageResource(R.drawable._00_pumpkin)
                "Broccoli" ->setImageResource(R.drawable._89_broccoli)
                "Cauliflower" ->setImageResource(R.drawable._02_cauliflower)
                "Courgette" ->setImageResource(R.drawable._03_zucchini)
                "Tomato" -> setImageResource(R.drawable._05_tomato)
                "Sweet Pepper" -> setImageResource(R.drawable._06_pepper)
                "Turnip" -> setImageResource(R.drawable._07_turnip)
                "Artichoke" -> setImageResource(R.drawable._09_artichoke)
                "Asparagus" -> setImageResource(R.drawable._10_asparagus)
                "Sweet Potato" -> setImageResource(R.drawable._11_sweet_potato)
                "Carrot" -> setImageResource(R.drawable._12_carrot)
                "Garlic" -> setImageResource(R.drawable._13_garlic)
                "Onion" -> setImageResource(R.drawable._56_onion)
                "Beetroot" -> setImageResource(R.drawable._15_beet)
                "Beans" -> setImageResource(R.drawable._16_beans)
                "Peas" -> setImageResource(R.drawable._17_soybean)
                "Aubergine" -> setImageResource(R.drawable._18_eggplants)
                "Cucumber" -> setImageResource(R.drawable._19_cucumber)
                "Lettuce" -> setImageResource(R.drawable._23_lettuce)
                "Radish" -> setImageResource(R.drawable._25_radish)
                "Chilli Pepper" -> setImageResource(R.drawable._27_chili_pepper)
                "Celery" -> setImageResource(R.drawable._30_celery)
                "Swiss Chard" -> setImageResource(R.drawable._31_chard)
                "Kale" -> setImageResource(R.drawable._33_kale)
                "Cabbage" -> setImageResource(R.drawable._34_cabbage)
                "Fennel" -> setImageResource(R.drawable._35_fennel)
                "Watermelon" -> setImageResource(R.drawable._36_watermelon)
                "Cantaloupe Melon" -> setImageResource(R.drawable._39_melon)
                "Strawberry" -> setImageResource(R.drawable._40_strawberry)
                "Apple" -> setImageResource(R.drawable._42_apple)
                "Lemon" -> setImageResource(R.drawable._43_lemon)
                "Pear" -> setImageResource(R.drawable._44_pear)
                "Red Cabbage" -> setImageResource(R.drawable._45_red_cabbage)
                "Peach" -> setImageResource(R.drawable._47_peach)
                "Orange" -> setImageResource(R.drawable._49_orange)
                "Cherry" -> setImageResource(R.drawable._50_cherry)
                "Fig" -> setImageResource(R.drawable._51_ficus)
                "Pomegranate" -> setImageResource(R.drawable._52_pomegranate)
                "Lime" -> setImageResource(R.drawable._53_lime)
                "Quince" -> setImageResource(R.drawable._60_quince)
                "Leek" -> setImageResource(R.drawable._67_leek)
                "Blackcurrant" -> setImageResource(R.drawable._68_berries)
                "Sunflower" -> setImageResource(R.drawable._71_sunflower)
                "Corn" -> setImageResource(R.drawable._76_corn)
                "Spinach" -> setImageResource(R.drawable._78_spinach)
                "Raspberry" -> setImageResource(R.drawable._80_raspberry)
                "Grape" -> setImageResource(R.drawable._82_grape)
                "Mushroom" -> setImageResource(R.drawable._83_mushroom)
                "Mediterranean Herbs" -> setImageResource(R.drawable._99_basil)

            }
        }

    }

}