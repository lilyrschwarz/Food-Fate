package edu.gwu.myapplication

import java.io.Serializable

data class RecipeData(
    val title: String,
    val description: String,
    val iconUrl: String
) : Serializable