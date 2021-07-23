package com.growingrubies.vegpatch.plantdetail

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

class PlantDetailViewModel(val database: PlantDatabaseDao,
                           application: Application
): AndroidViewModel(application) {

    //Encapsulated LiveData


    //Use repository instead of ViewModel directly
    private val plantRepository = PlantLocalRepository(database)

    init {

    }

    /**
     * Functions interacting with repository for database
     */


    /**
     * Navigation functions
     */

}