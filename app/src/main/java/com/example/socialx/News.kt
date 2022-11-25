package com.example.socialx

import org.json.JSONArray
import org.json.JSONObject

data class News(
    val title: String,
    val author: String,
    val description: String,
    val imageUrl: String,
    val url: String,
    val src : JSONObject,
    val publishedAt: String
)