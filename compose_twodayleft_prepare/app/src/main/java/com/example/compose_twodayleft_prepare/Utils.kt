package com.example.compose_twodayleft_prepare

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

const val hostname = "https://skills-music-api-v2.eliaschen.dev"

suspend fun getMusicApi(): List<MusicApi> {
    val client = OkHttpClient()
    val gson = Gson()
    var data: List<MusicApi>
    withContext(Dispatchers.IO) {
        val req = Request.Builder().url("$hostname/sounds").build()
        val res = client.newCall(req).execute()
        data = res.let {
            gson.fromJson(it.body?.string(), object : TypeToken<List<MusicApi>>() {}.type)
        }
    }
    return data
}