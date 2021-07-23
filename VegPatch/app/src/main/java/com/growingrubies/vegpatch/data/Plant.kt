package com.growingrubies.vegpatch.data

import java.io.Serializable

/**
 * data class acts as a data mapper between the DB and the UI
 */
data class Plant(
    val id: Long,
    val name: String
): Serializable