package com.growingrubies.vegpatch.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "plant_info_table")
data class PlantDTO (
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name ="icon") val icon: String,
    @ColumnInfo(name = "is_annual") val isAnnual: Boolean,
    @ColumnInfo(name = "is_frost_hardy") val isFrostHardy: Boolean,
    @ColumnInfo(name = "is_greenhouse_plant") val isGreenhousePlant: Boolean,
    @ColumnInfo(name = "sow_date") val sowDate: String?,
    @ColumnInfo(name = "plant_date") val plantDate: String?,
    @ColumnInfo(name = "harvest_date") val harvestDate: String,
    @ColumnInfo(name = "is_active_plant") val isActive: Boolean
)