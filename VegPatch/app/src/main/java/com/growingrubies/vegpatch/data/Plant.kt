package com.growingrubies.vegpatch.data

import java.io.Serializable

/**
 * data class acts as a data mapper between the DB and the UI
 */
data class Plant(
    val id: Long,
    val name: String,
    val icon: String,
    val annual: Boolean,
    val frostHardy: Boolean,
    val sowDate: String?,
    val plantDate: String?,
    val harvestDate: String,
    var active: Boolean = false
): Serializable

//Items for data class "Plant"
//id: Long
//name: String
//icon: String
//annual: Boolean
//frostHardy: Boolean
//sowDate: Date range (datatype???)
//plantDate: Date range (datatype???)
//harvestDate: Date range (datatype???)
//active: Boolean