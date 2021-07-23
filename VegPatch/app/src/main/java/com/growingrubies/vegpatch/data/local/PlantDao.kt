package com.growingrubies.vegpatch.data.local

import androidx.room.*
import com.growingrubies.vegpatch.data.Plant
import com.growingrubies.vegpatch.data.dto.PlantDTO

@Dao
interface PlantDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plant: PlantDTO)

    @Query("SELECT * FROM plant_info_table")
    suspend fun getAll(): List<PlantDTO>

    @Query("SELECT * FROM plant_info_table where id == :key")
    suspend fun get(key: Long): PlantDTO

}