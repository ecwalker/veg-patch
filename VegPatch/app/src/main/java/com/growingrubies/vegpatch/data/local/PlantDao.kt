package com.growingrubies.vegpatch.data.local

import androidx.room.*
import com.growingrubies.vegpatch.data.Plant
import com.growingrubies.vegpatch.data.dto.PlantDTO

@Dao
interface PlantDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(plant: PlantDTO)

    @Query("SELECT * FROM plant_info_table WHERE is_active_plant == 0 ORDER BY name")
    suspend fun getAll(): List<PlantDTO>

    @Query("SELECT * FROM plant_info_table WHERE is_active_plant == 1 ORDER BY name")
    suspend fun getActivePlants(): List<PlantDTO>

    @Query("SELECT * FROM plant_info_table where id == :key")
    suspend fun get(key: Long): PlantDTO

    @Query("UPDATE plant_info_table SET is_active_plant = 1 WHERE id == :key")
    suspend fun setPlantActive(key: Long)

    @Query("SELECT COUNT(*) FROM plant_info_table")
    suspend fun countNumPlants(): Int

    @Query("UPDATE plant_info_table SET is_active_plant = 0 WHERE id == :key")
    suspend fun removeActivePlant(key: Long)

}