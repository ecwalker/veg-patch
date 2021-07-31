package com.growingrubies.vegpatch.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "plant_info_table")
data class PlantDTO (
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name ="icon") val icon: String,
    @ColumnInfo(name = "annual") val annual: Boolean,
    @ColumnInfo(name = "frost_hardy") val frostHardy: Boolean,
    @ColumnInfo(name = "sow_date") val sowDate: String?,
    @ColumnInfo(name = "plant_date") val plantDate: String?,
    @ColumnInfo(name = "harvest_date") val harvestDate: String,
    @ColumnInfo(name = "active_plant") val active: Boolean
)