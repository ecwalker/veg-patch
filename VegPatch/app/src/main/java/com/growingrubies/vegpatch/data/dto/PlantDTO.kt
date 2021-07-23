package com.growingrubies.vegpatch.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "plant_info_table")
data class PlantDTO (
    //@PrimaryKey @ColumnInfo(name = "id") val id: Long,
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String
)